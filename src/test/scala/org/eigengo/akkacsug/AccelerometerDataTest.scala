package org.eigengo.akkacsug

import org.scalatest.{FlatSpec, Matchers}
import scodec.bits.BitVector

/**
 * Tests that the accelerometer data can be decoded from a stream constructed from
 *
 * {{{
 * #define GFS_HEADER_TYPE (uint16_t)0xfefc
 *
 * /**
 * * 5 B in header
 * */
 * struct __attribute__((__packed__)) gfs_header {
 *     uint16_t type;
 *     uint16_t count;
 *     uint8_t samples_per_second;
 * };
 *
 * /**
 * * Packed 5 B of the accelerometer values
 * */
 * struct __attribute__((__packed__)) gfs_packed_accel_data {
 *     int16_t x_val : 13;
 *     int16_t y_val : 13;
 *     int16_t z_val : 13;
 * };
 * }}}
 *
 * An example of the data is
 *
 * {{{
 * fcfe 0300 64 -> count = 3, samplesPerSecond = 100 (Hz)
 * ffff ffff 01 -> x =  -1, y =  -1, z =  127
 * 0000 0000 01 -> x =   0, y =   0, z =   64
 * 7801 4ac0 73 -> x = 376, y = 592, z = -784
 * fcfe 0100 64 -> count = 1, samplesPerSecond = 100 (Hz)
 * 7801 4ac0 73 -> x =  376, y = 592, z = -784
 * }}}
 *
 * And thus,
 *
 * {{{
 * gfs_header::type               = 0xfefc -> fcfe
 * gfs_header::count              = 0x0003 -> 0300
 * gfs_header::samples_per_second = 0x64   -> 0x64
 *
 * gfs_packed_accel_data::x       =  0 0001 0111 1000 -> 376
 * gfs_packed_accel_data::y       =  0 0010 0101 0000 -> 592
 * gfs_packed_accel_data::z       =  1 1100 1111 0000 -> -784
 * }}}
 */
class AccelerometerDataTest extends FlatSpec with Matchers {

  "Decoder" should "decode values" in {
    val bits = BitVector(
      0xfc, 0xfe, 0x03, 0x00, 0x64,
      0xff, 0xff, 0xff, 0xff, 0x01,
      0x00, 0x00, 0x00, 0x00, 0x01,
      0x78, 0x01, 0x4a, 0xc0, 0x73,
      0xfc, 0xfe, 0x01, 0x00, 0x64,
      0x78, 0x01, 0x4a, 0xc0, 0x73)

    val (BitVector.empty, ads) = AccelerometerData.decodeAll(bits, Nil)
    ads(0).values should contain allOf(AccelerometerValue(-1, -1, 127), AccelerometerValue(0, 0, 64), AccelerometerValue(376, 592, -784))
    ads(1).values should contain (AccelerometerValue(376, 592, -784))
  }

}
