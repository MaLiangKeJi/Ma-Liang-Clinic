<template>
  <el-container style="padding: 20px;">
    <el-header>
      <h2>功能：蓝牙连接手环，获得心率、血压、体温等信息。正在与手环进行连接调试，未完成</h2>
    </el-header>
    <el-main>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-button type="primary" @click="connectToDevice" :loading="loading">
            Connect to Device
          </el-button>
        </el-col>
        <el-col :span="16">
          <!-- 显示设备信息 -->
          <el-card v-if="deviceInfo" class="box-card">
            <div slot="header" class="clearfix">
              <span>Device Information</span>
            </div>
            <el-table :data="[deviceInfo]">
              <el-table-column prop="name" label="Device Name" width="180"></el-table-column>
              <el-table-column prop="id" label="Device ID" width="180"></el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
      <el-row :gutter="20" v-if="metrics.heartRate !== null">
        <el-col :span="24">
          <!-- 显示指标数据 -->
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>Metrics</span>
            </div>
            <el-table :data="[metrics]">
              <el-table-column prop="heartRate" label="Heart Rate" width="180"></el-table-column>
              <el-table-column prop="bloodPressure" label="Blood Pressure" width="180"></el-table-column>
              <el-table-column prop="bloodOxygen" label="Blood Oxygen" width="180"></el-table-column>
              <el-table-column prop="bodyTemperature" label="Body Temperature" width="180"></el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<script setup>
import { ref } from 'vue';

// 状态变量
const loading = ref(false);
const device = ref(null);
const server = ref(null);
const deviceInfo = ref(null);
const metrics = ref({
  heartRate: null,
  bloodPressure: null,
  bloodOxygen: null,
  bodyTemperature: null
});

// 蓝牙服务和特征值 UUID
const serviceUuid = '6e400001-b5a3-f393-e0a9-e50e24dcca9d';
const writeCharacteristicUuid = '6e400002-b5a3-f393-e0a9-e50e24dcca9d';
const notifyCharacteristicUuid = '6e400003-b5a3-f393-e0a9-e50e24dcca9d';
// CRC-16查找表
const crc16Table = [
        0x0000, 0xC0C1, 0xC181, 0x0140, 0xC301, 0x03C0, 0x0280, 0xC241,
        0xC601, 0x06C0, 0x0780, 0xC741, 0x0500, 0xC5C1, 0xC481, 0x0440,
        0xCC01, 0x0CC0, 0x0D80, 0xCD41, 0x0F00, 0xCFC1, 0xCE81, 0x0E40,
        0x0A00, 0xCAC1, 0xCB81, 0x0B40, 0xC901, 0x09C0, 0x0880, 0xC841,
        0xD801, 0x18C0, 0x1980, 0xD941, 0x1B00, 0xDBC1, 0xDA81, 0x1A40,
        0x1E00, 0xDEC1, 0xDF81, 0x1F40, 0xDD01, 0x1DC0, 0x1C80, 0xDC41,
        0x1400, 0xD4C1, 0xD581, 0x1540, 0xD701, 0x17C0, 0x1680, 0xD641,
        0xD201, 0x12C0, 0x1380, 0xD341, 0x1100, 0xD1C1, 0xD081, 0x1040,
        0xF001, 0x30C0, 0x3180, 0xF141, 0x3300, 0xF3C1, 0xF281, 0x3240,
        0x3600, 0xF6C1, 0xF781, 0x3740, 0xF501, 0x35C0, 0x3480, 0xF441,
        0x3C00, 0xFCC1, 0xFD81, 0x3D40, 0xFF01, 0x3FC0, 0x3E80, 0xFE41,
        0xFA01, 0x3AC0, 0x3B80, 0xFB41, 0x3900, 0xF9C1, 0xF881, 0x3840,
        0x2800, 0xE8C1, 0xE981, 0x2940, 0xEB01, 0x2BC0, 0x2A80, 0xEA41,
        0xEE01, 0x2EC0, 0x2F80, 0xEF41, 0x2D00, 0xEDC1, 0xEC81, 0x2C40,
        0xE401, 0x24C0, 0x2580, 0xE541, 0x2700, 0xE7C1, 0xE681, 0x2640,
        0x2200, 0xE2C1, 0xE381, 0x2340, 0xE101, 0x21C0, 0x2080, 0xE041,
        0xA001, 0x60C0, 0x6180, 0xA141, 0x6300, 0xA3C1, 0xA281, 0x6240,
        0x6600, 0xA6C1, 0xA781, 0x6740, 0xA501, 0x65C0, 0x6480, 0xA441,
        0x6C00, 0xACC1, 0xAD81, 0x6D40, 0xAF01, 0x6FC0, 0x6E80, 0xAE41,
        0xAA01, 0x6AC0, 0x6B80, 0xAB41, 0x6900, 0xA9C1, 0xA881, 0x6840,
        0x7800, 0xB8C1, 0xB981, 0x7940, 0xBB01, 0x7BC0, 0x7A80, 0xBA41,
        0xBE01, 0x7EC0, 0x7F80, 0xBF41, 0x7D00, 0xBDC1, 0xBC81, 0x7C40,
        0xB401, 0x74C0, 0x7580, 0xB541, 0x7700, 0xB7C1, 0xB681, 0x7640,
        0x7200, 0xB2C1, 0xB381, 0x7340, 0xB101, 0x71C0, 0x7080, 0xB041,
        0x5000, 0x90C1, 0x9181, 0x5140, 0x9301, 0x53C0, 0x5280, 0x9241,
        0x9601, 0x56C0, 0x5780, 0x9741, 0x5500, 0x95C1, 0x9481, 0x5440,
        0x9C01, 0x5CC0, 0x5D80, 0x9D41, 0x5F00, 0x9FC1, 0x9E81, 0x5E40,
        0x5A00, 0x9AC1, 0x9B81, 0x5B40, 0x9901, 0x59C0, 0x5880, 0x9841,
        0x8801, 0x48C0, 0x4980, 0x8941, 0x4B00, 0x8BC1, 0x8A81, 0x4A40,
        0x4E00, 0x8EC1, 0x8F81, 0x4F40, 0x8D01, 0x4DC0, 0x4C80, 0x8C41,
        0x4400, 0x84C1, 0x8581, 0x4540, 0x8701, 0x47C0, 0x4680, 0x8641,
        0x8201, 0x42C0, 0x4380, 0x8341, 0x4100, 0x81C1, 0x8081, 0x4040
];

// CRC-16校验码计算函数
const calculateCrc16 = (data) => {
    let crc = 0xFFFF; // 初始值
    for (let i = 0; i < data.length; i++) {
        let tableIndex = (crc ^ data[i]) & 0xFF;
        crc = (crc >> 8) ^ crc16Table[tableIndex];
    }
    return new Uint8Array([crc & 0xFF, (crc >> 8) & 0xFF]); // 返回小端模式的2字节CRC校验码
};


// 处理蓝牙设备断开连接事件
const handleDisconnected = () => {
  console.error('Bluetooth device disconnected, attempting to reconnect...');
  device.value.gatt.connect().then(() => {
    console.log('Device reconnected successfully.');
  }).catch(error => {
    console.error('Failed to reconnect:', error);
  });
};

// 连接到蓝牙设备并发送指令
const connectToDevice = async () => {
  loading.value = true;
  try {
    console.log('Requesting Bluetooth device...');
    
    const selectedDevice = await navigator.bluetooth.requestDevice({
      acceptAllDevices: true,
      optionalServices: [serviceUuid]
    });

    console.log('Device selected:', selectedDevice);

    // 监听设备断开事件
    selectedDevice.addEventListener('gattserverdisconnected', handleDisconnected);

    const gattServer = await selectedDevice.gatt.connect();
    console.log('Connected to GATT server:', gattServer);

    const primaryService = await gattServer.getPrimaryService(serviceUuid);
    console.log('Primary service obtained:', primaryService);

    const writeCharacteristic = await primaryService.getCharacteristic(writeCharacteristicUuid);
    const notifyCharacteristic = await primaryService.getCharacteristic(notifyCharacteristicUuid);

    await notifyCharacteristic.startNotifications();
    notifyCharacteristic.addEventListener('characteristicvaluechanged', handleNotifications);

    device.value = selectedDevice;
    server.value = gattServer;
    deviceInfo.value = {
      name: selectedDevice.name,
      id: selectedDevice.id
    };

    // 调用构建和发送指令的函数
    await buildAndSendCommand(writeCharacteristic);

  } catch (error) {
    console.error('Failed to connect or send command:', error);
  } finally {
    loading.value = false;
  }
};

// 构建并发送完整的指令
const buildAndSendCommand = async (writeCharacteristic) => {
  // 如果设备断开连接，尝试重新连接
  if (!device.value.gatt.connected) {
    console.log('Device disconnected, attempting to reconnect...');
    try {
      await device.value.gatt.connect();
      console.log('Device reconnected successfully.');
    } catch (error) {
      console.error('Failed to reconnect before sending command:', error);
      return;
    }
  }

  // 构建发送的指令
  const protocolHeader = 0xDA;
  const command = 0x0D;
  const dataLength = [0x00, 0x00];

  const commandWithoutCrc = new Uint8Array([protocolHeader, command, ...dataLength]);

  // 计算CRC校验码
  const crc = calculateCrc16(commandWithoutCrc);

  const fullCommand = new Uint8Array([...commandWithoutCrc, ...crc]);

  console.log('发送的完整指令（十六进制）:', fullCommand.map(byte => byte.toString(16)).join(' '));

  try {
    await writeCharacteristic.writeValue(fullCommand.buffer);
    console.log('Command sent to device.');
  } catch (error) {
    console.error('Failed to send command:', error);
  }
};


// 处理蓝牙设备返回的通知数据
const handleNotifications = (event) => {
    const value = event.target.value;
    const data = new Uint8Array(value.buffer);

    console.log('接收到的数据（十进制）:', data);
    console.log('接收到的数据（十六进制）:', data.map(byte => byte.toString(16)).join(' '));

    // 确认数据长度是否合理
    if (data.length < 4) {
        console.error('接收到的数据长度不足，无法解析。');
        return;
    }

    const cmd = data[1];
    if (cmd !== 0x8D) {
        console.error(`指令码不匹配，期望值: 0x8D，实际值: 0x${cmd.toString(16)}`);
        return;
    }

    const dataLength = data[2] + (data[3] << 8);
    console.log(`数据长度: ${dataLength} 字节`);

    const payload = data.slice(4, 4 + dataLength);
    const deviceCrc = data.slice(-2); // 最后两个字节为 CRC 校验码

    console.log('数据段（payload）:', payload.map(byte => byte.toString(16)).join(' '));

    // 解析各项健康数据
    const heartRate = payload[12]; // 当前心率
    const bloodPressureLow = payload[13]; // 低血压
    const bloodPressureHigh = payload[14]; // 高血压
    const bodyTemperature = (payload[18] + (payload[19] << 8)) / 100; // 体温，单位是0.01度

    console.log(`心率: ${heartRate}`);
    console.log(`血压: ${bloodPressureHigh}/${bloodPressureLow}`);
    console.log(`体温: ${bodyTemperature}`);

    // 这里你可以将解析到的数据存储到UI显示变量中
    metrics.heartRate = heartRate;
    metrics.bloodPressure = `${bloodPressureHigh}/${bloodPressureLow}`;
    metrics.bodyTemperature = bodyTemperature;

    const calculatedCrc = calculateCrc16(data.slice(0, -2));
    console.log('计算的 CRC 校验码:', calculatedCrc.map(byte => byte.toString(16)).join(' '));

    const isCrcValid = calculatedCrc[0] === deviceCrc[0] && calculatedCrc[1] === deviceCrc[1];
    console.log('CRC 校验码是否正确:', isCrcValid);

    if (!isCrcValid) {
        console.error('CRC 校验码不匹配，数据可能被损坏。');
    } else {
        console.log('CRC 校验成功，继续解析数据。');
    }
};
</script>

<style scoped>
.el-container {
  margin: 20px;
}
.el-header {
  font-size: 20px;
  text-align: center;
  margin-bottom: 20px;
}
.el-button {
  width: 200px;
}
.box-card {
  margin-bottom: 20px;
}
</style>
