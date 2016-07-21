import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ByteTest {
	public static void main(String[] args) {
		long time = new Date().getTime();
//		startRecord(1449630298993L, 1449630298993L);
		long t = 1449630298993L;
//		System.out.println(t / 1000);
		byte[]bytes1 = buildRecordData(t, t);
		byte[]bytes2 = buildRecordBytes(t, t);
		System.out.println(Arrays.toString(bytes1));
		System.out.println(Arrays.toString(bytes2));
		long t2 = 1449630298L*1000;
		System.out.println(new Date(t2));
	}

	public static byte[] buildRecordData( long startTime, long nowTime) {
		// 将毫秒改为秒数
		byte[] data = new byte[18];
		int sTime = (int) (startTime / 1000);
		int nTime = (int) (nowTime / 1000);
		byte[] sData = BytesUtil.getBytes(sTime);
		byte[] nData = BytesUtil.getBytes(nTime);

		data[0] = (byte) 0xFE;
		// head
		data[1] = 0x01;
		// 序列号
		data[2] = 0x00;
		data[3] = 0x01;
		// 数据长度
		data[4] = 0x00;
		data[5] = 0x0A;
		// 数据
		data[6] = 0x04;// command
		// 开始时间
		data[7] = sData[0];
		data[8] = sData[1];
		data[9] = sData[2];
		data[10] = sData[3];
		// 当前时间
		data[11] = nData[0];
		data[12] = nData[1];
		data[13] = nData[2];
		data[14] = nData[3];
		// interval
		data[15] = 0x01;
		//
		byte[] crcData = Arrays.copyOfRange(data, 1, 16);
		short crc = (short) CRC16.calcCrc16(data, 0, 16);
		data[16] = (byte) (crc & 0xff);
		data[17] = (byte) ((crc & 0xff00) >> 8);
		return data;
	}

	public static byte[] buildRecordBytes(long startTime, long nowTime) {
		int iStartTime = (int) (startTime / 1000);
		int iNowTime = (int) (nowTime / 1000);
		byte[] data1 = BytesUtil.getBytes(iStartTime);
		byte[] data2 = BytesUtil.getBytes(iNowTime);
		byte[] data3 = new byte[data1.length + data2.length+1];
		System.arraycopy(data1, 0, data3, 0, data1.length);
		System.arraycopy(data2, 0, data3, data1.length, data2.length);
		data3[8] = 0x01;
		byte command = 0x04;
		return buildCommandBytes(command, data3);
	}

	public static byte[] buildFileDownloadBytes(long startTime) {
		int iTime = (int) (startTime / 1000);
		byte[] data = BytesUtil.intToBytes(iTime);
		byte command = 0x01;
		return buildCommandBytes(command, data);
	}

	public static byte[] buildCommandBytes(byte command, byte[] data) {
		int len = 9 + data.length;
		System.out.println(len);
		byte[] bytes = new byte[len];
		bytes[0] = (byte) 0xFE;
		// head
		bytes[1] = 0x01;
		// 序列号
		bytes[2] = 0x00;
		bytes[3] = 0x01;
		short dataLen = (short) (len - 8);
		System.out.println("dataLen:"+dataLen);
		// 数据长度
		bytes[5] = (byte) (dataLen & 0xff);
		bytes[4] = (byte) ((dataLen & 0xff00) >> 8);
		// command
		bytes[6] = command;
		for (int i = 7; i < 7 + data.length; i++) {
			bytes[i] = data[i - 7];
		}
		// crc
		short crc = (short) CRC16.calcCrc16(bytes, 0, len - 2);
		bytes[len - 2] = (byte) (crc & 0xff);
		bytes[len - 1] = (byte) ((crc & 0xff00) >> 8);
		return bytes;
	}

	public static int bytesToInt(byte[] ary, int offset) {
		int value;
		value = (int) ((ary[offset] & 0xFF) | ((ary[offset + 1] << 8) & 0xFF00)
				| ((ary[offset + 2] << 16) & 0xFF0000) | ((ary[offset + 3] << 24) & 0xFF000000));
		return value;
	}

	/**
	 * 开始记录数据
	 */
	public static void startRecord(long startTime, long nowTime) {
		// 将毫秒改为秒数
		int sTime = (int) (startTime / 1000);
		int nTime = (int) (nowTime / 1000);
		byte[] sData = BytesUtil.getBytes(sTime);
		byte[] nData = BytesUtil.getBytes(nTime);
		byte b = (byte) 0xFE;
		byte[] data = new byte[18];
		data[0] = (byte) 0xFE;
		// head
		data[1] = 0x01;
		// 序列号
		data[2] = 0x00;
		data[3] = 0x01;
		// 数据长度
		data[4] = 0x00;
		data[5] = 0x0A;
		// 数据
		data[6] = 0x04;// command
		// 开始时间
		data[7] = sData[0];
		data[8] = sData[1];
		data[9] = sData[2];
		data[10] = sData[3];
		// 当前时间
		data[11] = nData[0];
		data[12] = nData[1];
		data[13] = nData[2];
		data[14] = nData[3];
		// interval
		data[15] = 0x01;
		//
		byte[] crcData = Arrays.copyOfRange(data, 1, 16);
		System.out.println(Arrays.toString(data));
		System.out.println(Arrays.toString(crcData));
		short crc = (short) CRC16.calcCrc16(data, 1, 15);
		data[16] = (byte) (crc & 0xff);
		data[17] = (byte) ((crc & 0xff00) >> 8);
		System.out.println(Arrays.toString(data));
		System.out.println(new Date(1449649679L * 1000));
	}

}
