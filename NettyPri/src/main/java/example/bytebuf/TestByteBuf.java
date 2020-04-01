package example.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

public class TestByteBuf {


    public static void main(String[] args) {
        //- 创建一个非池化的bytebuf
        ByteBuf buf = Unpooled.buffer(10);

        //- rw指针都在0
        System.out.println("原始的bytebuf为："+buf.toString());
        //- 查看bytebuf中的内容，初始化，都为0
        System.out.println("bytebuf中的内容为："+ Arrays.toString(buf.array())+"\n");


        //- 将数组写入到buf中,着重观察写指针的移动
        byte[] bytes = {1,2,3,4,5};
        buf.writeBytes(bytes);

        System.out.println("写入的bytes为："+Arrays.toString(buf.array()));
        System.out.println("写入bytes以后的指针为："+buf.toString()+"\n");



        //- 将buf中的数据读出到byte中，着重观察读指针的移动
        byte b1 = buf.readByte();
        byte b2 = buf.readByte();

        System.out.println("读取后的buf为："+Arrays.toString(buf.array()));
        System.out.println("读取后的buf指针为："+buf.toString()+"\n");




        //- 将已读的byte内容丢弃,着重观察读指针的移动以及丢弃后的内容34545，丢弃以后，内容会向前覆盖
        buf.discardReadBytes();
        System.out.println("读取丢弃后的buf为："+Arrays.toString(buf.array()));
        System.out.println("读取丢弃后的buf指针为："+buf.toString()+"\n");


        //- 清空读写指针，只清空指针，不清空数据
        buf.clear();
        System.out.println("清空后的内容为："+Arrays.toString(buf.array()));
        System.out.println("清空后的指针为："+buf.toString()+"\n");


        //- 再次写入数据，覆盖数据，只在乎指针，不在乎数据
        byte[] bytes2 = {1,2,3};
        buf.writeBytes(bytes2);
        System.out.println("再次写入后的内容为："+Arrays.toString(buf.array()));
        System.out.println("再次写入后的指针为："+buf.toString()+"\n");


        //- 清空buf内容，只清空内容，不清空指针
        buf.setZero(0,buf.capacity());
        System.out.println("清空内容后的内容为："+Arrays.toString(buf.array()));
        System.out.println("清空内容后的指针为："+buf.toString()+"\n");



        //- buf的自动扩容
        buf.clear();
        byte[] byteExpand = {1,2,3,4,5,6,7,8,9,10,11,12};
        buf.writeBytes(byteExpand);
        System.out.println("清空内容后的内容为："+Arrays.toString(buf.array()));
        System.out.println("清空内容后的指针为："+buf.toString()+"\n");
    }

}
