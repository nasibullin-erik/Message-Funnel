package ru.itis.mfbotsapi.api.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.itis.mfbotsapi.api.exceptions.ClientDisconnectException;
import ru.itis.mfbotsapi.api.exceptions.IncorrectFCSException;
import ru.itis.mfbotsapi.api.exceptions.TCPFrameFactoryException;

import java.io.*;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.IllegalBlockingModeException;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Data
public class TCPFrameFactoryImpl implements TCPFrameFactory {

    protected byte pr;
    protected byte sdf;
    protected int maxLength;
    protected int maxTypeValue;
    protected int minTypeValue;
    protected Map<Long, ObjectInputStream> streams;

    public TCPFrameFactoryImpl(byte pr, byte sdf, int maxLength, int maxTypeValue, int minTypeValue){
        this.pr = pr;
        this.sdf = sdf;
        this.maxLength = maxLength;
        this.maxTypeValue = maxTypeValue;
        this.minTypeValue = minTypeValue;
        this.streams = new HashMap<>();
    }

    @Override
    public TCPFrame createTCPFrame(int messageType, Object... messageContent) {
        return new TCPFrame(this, messageType, messageContent);
    }

    @Override
    public TCPFrame readTCPFrame(SocketChannel channel) throws TCPFrameFactoryException, IncorrectFCSException {
        TCPFrame result = null;
        try{
//            ObjectInputStream inObject = streams.get(Thread.currentThread().getId());
//            if (inObject == null){
//                inObject = new ObjectInputStream(channel.socket().getInputStream());
//                streams.put(Thread.currentThread().getId(), inObject);
//            }
//            byte framePr = inObject.readByte();
//            byte frameSdf = inObject.readByte();
//            if ((framePr == pr)&&(frameSdf == sdf)) {
//                byte type = inObject.readByte();
//                int dataLength = inObject.readShort();
//                inObject.mark(dataLength + 2);
//                boolean isCorrect = checkFcs(inObject, type, (short) dataLength);
//                inObject.reset();
//                if (isCorrect){
//                    Object[] objects;
//
//                    switch (type){
//                        case 1:
//                            objects = new Object[2];
//                            objects[0] = inObject.readObject(); //Message id
//                            objects[1] = inObject.readObject(); //Bot token
//                            break;
//                        case 2:
//                            objects = new Object[3];
//                            objects[0] = inObject.readObject(); //Message id
//                            objects[1] = inObject.readObject(); //Messenger type
//                            objects[2] = inObject.readObject(); //Bot name
//                            break;
//                        case 3:
//                            objects = new Object[4];
//                            objects[0] = inObject.readObject(); //Message id
//                            objects[1] = inObject.readObject(); //User id
//                            objects[2] = inObject.readObject(); //User nickname
//                            objects[3] = inObject.readObject(); //Message text
//                            break;
//                        case 4:
//                            objects = new Object[3];
//                            objects[0] = inObject.readObject(); //Message id
//                            objects[1] = inObject.readObject(); //User id
//                            objects[2] = inObject.readObject(); //Reply text
//                            break;
//
//                        default:
//                            objects = new Object[0];
//                            break;
//                    }
//
//                    result = new TCPFrame(this, type, objects);
//                } else {
//                    throw new IncorrectFCSException(inObject.readObject());
//                }
//          ============================================================================================================
            ByteBuffer serviceBytesBuffer = ByteBuffer.allocate(5);
            channel.read(serviceBytesBuffer);
            serviceBytesBuffer.flip();
            byte framePr = serviceBytesBuffer.get();
            byte frameSdf = serviceBytesBuffer.get();
            if ((framePr == pr)&&(frameSdf == sdf)){
                byte type = serviceBytesBuffer.get();
                int dataLength = serviceBytesBuffer.getShort();
                ByteBuffer recvDataBuffer = ByteBuffer.allocate(dataLength + 1);
                channel.read(recvDataBuffer);
                byte[] recvData = recvDataBuffer.array();
                int currentSum = pr + sdf + type + dataLength;
                for (int i = 0; i < dataLength; i++){
                    currentSum+=recvData[i];
                }
                byte fcs = (byte) (currentSum%3 << 6 | currentSum%5 << 3 | currentSum%7);
                ByteArrayInputStream byteStream = new ByteArrayInputStream(Arrays.copyOfRange(recvData, 0, recvData.length));
                ObjectInputStream inObject = new ObjectInputStream(new BufferedInputStream(byteStream));
                Object[] objects;

                if (recvData[recvData.length-1] == fcs){
                    switch (type){
                        case 1:
                            objects = new Object[2];
                            objects[0] = inObject.readObject(); //Message id
                            objects[1] = inObject.readObject(); //Bot token
                            break;
                        case 2:
                            objects = new Object[3];
                            objects[0] = inObject.readObject(); //Message id
                            objects[1] = inObject.readObject(); //Messenger type
                            objects[2] = inObject.readObject(); //Bot name
                            break;
                        case 3:
                            objects = new Object[4];
                            objects[0] = inObject.readObject(); //Message id
                            objects[1] = inObject.readObject(); //User id
                            objects[2] = inObject.readObject(); //User nickname
                            objects[3] = inObject.readObject(); //Message text
                            break;
                        case 4:
                            objects = new Object[3];
                            objects[0] = inObject.readObject(); //Message id
                            objects[1] = inObject.readObject(); //User id
                            objects[2] = inObject.readObject(); //Reply text
                            break;

                        default:
                            objects = new Object[0];
                            break;
                    }
                } else {
                    throw new IncorrectFCSException(inObject.readObject());
                }
                inObject.close();
                byteStream.close();
                  result = new TCPFrame(this, type, objects);
            }
            return result;
        } catch (IOException ex) {
            throw new TCPFrameFactoryException("Cannot read TCP frame.", ex);
        } catch (ClassNotFoundException ex){
            throw new TCPFrameFactoryException("Unknown class in frame content", ex);
        }
    }

    protected boolean checkFcs(ObjectInputStream stream, byte type, short length) throws IOException {
        int currentSum = pr + sdf + type + length;
        for (int i = 0; i < length; i++){
            currentSum += stream.readByte();
        }
        byte fcs = stream.readByte();
        byte currentFcs = (byte) (currentSum%3 << 6 | currentSum%5 << 3 | currentSum%7);
        return fcs == currentFcs;
    }

    @Override
    public void writeTCPFrame(SocketChannel channel, TCPFrame tcpFrame) throws TCPFrameFactoryException {
        try{
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream(maxLength);
            ObjectOutputStream outObject = new ObjectOutputStream(new BufferedOutputStream(byteStream));
            for (int i = 0; i < tcpFrame.getContent().length; i++){
                outObject.writeObject(tcpFrame.getContent()[i]);
            }
            outObject.flush();
            outObject.close();
            byte[] sendBuf = byteStream.toByteArray();
            int currentSum = pr + sdf + tcpFrame.getType() + sendBuf.length;
            for (int i = 0; i < sendBuf.length; i++){
                currentSum+=sendBuf[i];
            }
            byte fcs = (byte) (currentSum%3 << 6 | currentSum%5 << 3 | currentSum%7);

            ByteBuffer sendData = ByteBuffer.allocate(5 + sendBuf.length + 1);
            sendData.put(pr);
            sendData.put(sdf);
            sendData.put((byte) tcpFrame.getType());
            sendData.putShort((short) sendBuf.length);
            sendData.put(sendBuf);
            sendData.put(fcs);
            sendData.flip();
            channel.write(sendData);

        }catch (IOException ex) {
            throw new TCPFrameFactoryException("Cannot send TCP message", ex);
        }
    }
}
