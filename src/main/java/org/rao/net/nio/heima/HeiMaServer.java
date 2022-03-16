package org.rao.net.nio.heima;

import com.alibaba.fastjson.JSON;
import javafx.concurrent.Worker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * 使用 Nio 来理解阻塞模式
 *
 *  下方使用的 ：
 *      单线程的Reactor模型  （无法承载高并发）
 *      还有：
 *      单Acceptor线程多IO线程的Reactor模型
 *      多Acceptor线程多IO线程的Reactor模型
 *
 * @author Rao
 * @Date 2021-10-22
 **/
public class HeiMaServer {


    // 这是 有问题的
    private static ByteBuffer byteBuffer = ByteBuffer.allocate(50);


    /**
     * 阻塞的 socketChannel
     */
    public static void blockNio() throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress( 8888));
        System.out.println("in.............");
        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept();
            System.out.println("accepted.............");

            socketChannel.read( byteBuffer);
            System.out.println("client msg:"+ new String(byteBuffer.array()));

            socketChannel.write( Charset.defaultCharset().encode("server receive"));
            System.out.println("socketChannel.close().............");

        }

    }

    /**
     * 不阻塞 socketChannel
     * @throws Exception
     */
    public static void noBlock() throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress( 8888));
        // 异步
        serverSocketChannel.configureBlocking(false);
        System.out.println("in.............");
        while (true){
            //serverSocketChannel 配置异步时，SocketChanel可能为null
            SocketChannel socketChannel = serverSocketChannel.accept();
            System.out.println("accepted.............");

            if(socketChannel != null){
                // 异步
                socketChannel.configureBlocking(false);

                // 当socketChannel 配置异步时，read 可能为0
                int read = socketChannel.read(byteBuffer);
                System.out.println("client read byte: " + read);
                if( read > 0){
                    System.out.println("client msg:"+ new String(byteBuffer.array()));
                }

                socketChannel.write( Charset.defaultCharset().encode("server receive"));
                System.out.println("socketChannel.close().............");
            }

        }
    }





    /**
     * 单线程的Reactor Nio模型
     */
    public static void selectorNio() throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress( 8888));
        // 异步
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();

        // 注册多个事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("in.............");
        // selector.select  返回可执行的事件 >>  需要留意的是 当前方法 在 没有事件发生时 是阻塞的，
        // 当 还存有事件未消费时 ，这个 是非阻塞得，光靠 迭代器remove 是不行的，因为整体实现是有 两个 队列，第一个管理 谁注册到 这个 selector上，另外一个是管理  发生事件；
        // 因此 当一个事件 发生问题时 ，key需要取消，客户端异常断开会触发一个 读事件。
        while (selector.select() > 0) {
            System.out.println("selected.........");

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 可接收事件
                if (selectionKey.isAcceptable()) {
                    // 可接收事件
//                    System.out.println("isAcceptable........");
                    // 这个值就是  服务器本身 ： serverSocketChannel
//                    ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                    // selectableChannel ---- sun.nio.ch.ServerSocketChannelImpl[/0:0:0:0:0:0:0:0:8888]
                    // 这里是否阻塞和 serverSocketChannel.configureBlocking( false); 这里有关
//                    System.out.println("scc.accept .........");
                    // 异步
//                    SocketChannel socketChannel = ssc.accept();
                    // 异步  怎么说呢？ 这里发生时已经准备好了，所以无论咋样都是会读出来的。
                    SocketChannel socketChannel = serverSocketChannel.accept();
//                    System.out.println("scc.accepted .........");

                    // 这里设置为 false 后， socketChannel.read 就是非阻塞的了  所以这里可能为null  出现空指针的问题不管客户端是否断开，
                    socketChannel.configureBlocking(false);
                    // socketChannel 客户端 注册 可读事件 未注册

                    // 关联上 附件
                    ByteBuffer allocate = ByteBuffer.allocate(4);

                    // 应当遵循 读写的规律  读了 要写 ，写了 再读，
                    socketChannel.register(selector, SelectionKey.OP_READ,allocate);
//                    System.out.println("isAcceptable.........end ");
                }

                else if( selectionKey.isReadable()){
                    // 可读事件是 对方客户端只要 往server 写数据 就会触发，不用多次注册同一事件
                    System.out.println("isReadable........");
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    //socketChannel.isOpen() && socketChannel.isConnected() && 这些参数 控制不了！！！
                    // 客户端结束会导致这里抛异常！  但是 客户端调用socketChannel.close(); 可以控制

                    try {
//                        System.out.println("client >>  isOpen :" + socketChannel.isOpen() + "; isConnected :"+socketChannel.isConnected() + "" +
//                                "; isBlocking:" + socketChannel.isBlocking() + "; finishConnect" + socketChannel.finishConnect() );

                        // 这个值 为null
                        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();

                        // 这里需要处理一个拆包和粘包的问题  与读取扩容问题
                        // 拆包 和 粘包可以使用 netty的 根据集来处理
                        int read = socketChannel.read( byteBuffer);
                        if( read == -1){
                            // 客户端正常断开
                            System.out.println("client Actively closed!");
                            selectionKey.cancel();
                            socketChannel.close();
                        }else {
                            // 说明满了
                            if( byteBuffer.position() == byteBuffer.limit()){
                                ByteBuffer allocate = ByteBuffer.allocate(byteBuffer.position() * 2);
                                byteBuffer.flip();
                                allocate.put( byteBuffer);
                                selectionKey.attach( allocate);
                            }else{

                                System.out.println("client read byte: " + read);
                                System.out.println("client msg: " + new String(byteBuffer.array()));
                                // 加快回收
                                byteBuffer.clear();
                                System.out.println("isReadable........end");

                                selectionKey.attach( null);

                                // 使用 socketChanel 注册 会 丢失 附件信息
                                // 而使用 selectionKey.interestOps 则 会保留在 这里 。
                                
                                socketChannel.register( selector,SelectionKey.OP_WRITE);


                            }

                        }
                    } catch (IOException ie){
                        // 客户端异常断开
                        System.out.println(" client write disconnected!");
                        selectionKey.cancel();
                        socketChannel.close();
                    }

                }

                else if( selectionKey.isWritable()){
                    System.out.println("isWritable........");
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    try {

                        ByteBuffer oldByteBuffer = (ByteBuffer) selectionKey.attachment();
                        // 一次 写 完了 ... .
                        if(oldByteBuffer != null && oldByteBuffer.hasRemaining()){
                            // 这里没进
                            int write = socketChannel.write(oldByteBuffer);
                            if ( write == oldByteBuffer.limit()) {
                                System.out.println("1 Finished..........");
                                // 帮助回收
                                oldByteBuffer.clear();
                                // 监听 读事件
                                ByteBuffer allocate = ByteBuffer.allocate(4);
                                selectionKey.attach( allocate);
                                selectionKey.interestOps( SelectionKey.OP_READ);
                            }
                        }
                        // 需要移除
                        else{

                            // 新 客户端 写数据
                            StringBuilder str = new StringBuilder();
                            // 太小了 要很大很大  java.lang.OutOfMemoryError: Java heap space
                            for (int i = 0; i < 500000; i++) {
                                str.append("a");
                            }

                            ByteBuffer byteBuffer = Charset.defaultCharset().encode(str.toString());
                            // 这里 一次 性写完了   为什么这里 一次性写完了
                            int write = socketChannel.write( byteBuffer);

                            // 优化 继续写
//                        while ( byteBuffer.hasRemaining()){
//                            socketChannel.write( byteBuffer);
//                        }

                            if( byteBuffer.hasRemaining()){
                                selectionKey.attach( byteBuffer);
                            }else{
                                System.out.println("2 Finished..........");
                                // 帮助回收
                                // 监听 读事件
                                ByteBuffer allocate = ByteBuffer.allocate(4);
                                selectionKey.attach( allocate);
                                selectionKey.interestOps( SelectionKey.OP_READ);
                            }

                            System.out.println("client read byte: " + write);
//                        byteBuffer.clear();
//
//                        // 把事件关了
//                        selectionKey.cancel();


                            System.out.println("isWritable........end");
                        }

                    }catch (IOException ie){
                        System.out.println(" client read disconnected!");
                        selectionKey.cancel();
                        socketChannel.close();
                    }

                }

                iterator.remove();

            }
            System.out.println("selected.........end");

        }
    }


    /**
     * 学习 某个东东
     * @throws Exception
     */
    public static void studyWakeup() throws Exception{

        // Boss
        Selector bossSelector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind( new InetSocketAddress(8888));
        serverSocketChannel.register( bossSelector,SelectionKey.OP_ACCEPT);

        new Thread(() -> {

            try {
                Scanner scanner = new Scanner(System.in);
                while (!"1".equals(scanner.nextLine())){

                    // 唤醒阻塞 在 select的线程
                    bossSelector.wakeup();
                }

            } catch (Exception e){

            }

        }).start();

        while (true){

            // 会被  wakeup方法唤醒
            bossSelector.select();
            System.out.println(" i be live!");
        }

    }


    /**
     *  单accept线程 多IO 线程的reactor 模型
     *  需要注意的地方：
     *  1、register 方法和 select 方法之间会互相等待，形成死锁。
     *  设计要点：
     *  1、主线程 处理 accept 事件，接收后交给 Worker IO线程 处理。
     *  2、每一个Worker 对应一个 Selector 选择器。
     *  3、此外，没一个 Worker 应当还需要再添加一个 队列维护每一个 接收的客户端。
     *  4、这里有利用到 selector.wakeup 方法来唤醒 阻塞在 selector.select() 方法的
     */
    public static void multiWorkerSelector() throws Exception {

        // Boss
        Selector bossSelector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.bind( new InetSocketAddress(8888));
        serverSocketChannel.register( bossSelector,SelectionKey.OP_ACCEPT);

        // 这个值会不会封顶呢？
        AtomicInteger atomicInteger = new AtomicInteger( Runtime.getRuntime().availableProcessors());
        Worker[] workers = new Worker[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < atomicInteger.get() ; i++) {
            workers[i] = new Worker(i+"");
        }


        while (true){
            bossSelector.select();

            Iterator<SelectionKey> iterator = bossSelector.selectedKeys().iterator();
            while ( iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();

                // 主线程 只处理 连接事件
                if ( selectionKey.isAcceptable()) {

                    // 这是 一个 客户端
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);

                    // 把客户端交给 另外一个 Selector处理。
                    workers[atomicInteger.getAndIncrement()%workers.length].register(socketChannel);

                }

                iterator.remove();
            }


        }




    }

    /**
     * Worker
     */
    static class Worker implements Runnable{

        // 看到 volatile 一定要想到 happen-before 原则 通常我的理解是 A结果可见于B前置执行，miss 不能指令重排。
        private volatile boolean start;

        // 创建 Worker 时 不初始化相关参数 ，等真正有事件来临时再创建，似乎也不是很好
        private Selector selector;
        private Thread thread;
        private ConcurrentLinkedQueue<Runnable> queue;
        // 当前Worker的name
        private String name;

        /**
         * 初始化就开始初始化了！
         */
        public Worker(String name) throws Exception {
            this.name = "worker-" + name;
            this.thread = new Thread(this);
            this.queue = new ConcurrentLinkedQueue<>();
            this.selector = Selector.open();

            //浪费资源
            this.thread.start();
        }

        public void register(SocketChannel socketChannel){

            // register 与 selector.select 会导致阻塞的问题发生
            // 这个是 和 Semaphore 信号量是类似的 ， 也类似于 LockSupport

            // 利用队列来执行
            queue.add( () -> {
                System.out.println("register : " + name );
                try {
                    socketChannel.register( selector,SelectionKey.OP_READ);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
                System.out.println("registered : " + name );
            });

            // 阻塞在这了  目的是 需要这里先 注册进去  这样似乎是 不可以的 ...
            selector.wakeup();

        }

        @Override
        public void run() {
            
            try {
                while (true){
                    // 会阻塞
                    selector.select();

                    Iterator<Runnable> taskIterator = queue.iterator();
                    while ( taskIterator.hasNext()){
                        taskIterator.next().run();
                        taskIterator.remove();
                    }

                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while ( iterator.hasNext()){
                        SelectionKey selectionKey = iterator.next();
                        iterator.remove();

                        try {
                            // 处理读事件
                            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                            ByteBuffer allocate = ByteBuffer.allocate(50);
                            socketChannel.read( allocate);
                            System.out.println(name +  " , client msg:" + new String( allocate.array()));
                        }catch (IOException ie){
                            System.out.println(name + ", client read disconnected!");
                            selectionKey.cancel();
                        }
                    }
                }

            } catch (IOException e) {
                try {
                    selector.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                e.printStackTrace();
            }

        }


    }


    /**
     * 多接收器多Io线程 处理 模式
     */
    public static void multiAcceptAndMultiIoReactor(){

    }


    /**
     * 信号量学习
     */
    public static void studySemaphore() throws Exception {
        Semaphore semaphore = new Semaphore(0);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);

                semaphore.release();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.setDaemon( true);
        thread.start();

        // 此方法一定是阻塞的
        System.out.println("11111:" + System.currentTimeMillis());
        semaphore.acquire();
        System.out.println("22222:" + System.currentTimeMillis());


    }

    /**
     * 学习LockSupport
     */
    public static void studyLockSupport(){
        Thread boss = Thread.currentThread();

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000);

                LockSupport.unpark( boss);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.setDaemon( true);
        thread.start();

        // 此方法一定是阻塞的
        System.out.println("11111:" + System.currentTimeMillis());
        LockSupport.park();
        System.out.println("22222:" + System.currentTimeMillis());
    }


    /**
     * 粘包、半粘包处理
     * @param buffer
     */
    private static void split(ByteBuffer buffer) {
        buffer.flip();
        for(int i = 0; i < buffer.limit(); i++) {
            // 遍历寻找分隔符
            // get(i)不会移动position
            if (buffer.get(i) == '\n') {
                // 缓冲区长度
                int length = i+1-buffer.position();
                ByteBuffer target = ByteBuffer.allocate(length);
                // 将前面的内容写入target缓冲区
                for(int j = 0; j < length; j++) {
                    // 将buffer中的数据写入target中
                    target.put(buffer.get());
                }
                // 打印结果
                System.out.println("msg :" + new String( target.array()));
            }
        }
        // 切换为写模式，但是缓冲区可能未读完，这里需要使用compact
        buffer.compact();
    }


    public static void main(String[] args) throws Exception{
//        blockNio();
//        noBlock();

        // 单线程的
//        selectorNio();

        // 学习 唤醒
//        studyWakeup();

        // 单 接收线程多io线程的

        multiWorkerSelector();





    }
}
