// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/terminal/common/java/FormatEncoding.proto

package com.navinfo.opentsp.platform.location.protocol.terminal.common;

public final class LCFormatEncoding {
  private LCFormatEncoding() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public enum FormatEncoding
      implements com.google.protobuf.ProtocolMessageEnum {
    picture_jpeg(0, 0),
    picture_tif(1, 1),
    audio_mp3(2, 2),
    audio_wav(3, 3),
    video_wmv(4, 4),
    ;
    
    public static final int picture_jpeg_VALUE = 0;
    public static final int picture_tif_VALUE = 1;
    public static final int audio_mp3_VALUE = 2;
    public static final int audio_wav_VALUE = 3;
    public static final int video_wmv_VALUE = 4;
    
    
    public final int getNumber() { return value; }
    
    public static FormatEncoding valueOf(int value) {
      switch (value) {
        case 0: return picture_jpeg;
        case 1: return picture_tif;
        case 2: return audio_mp3;
        case 3: return audio_wav;
        case 4: return video_wmv;
        default: return null;
      }
    }
    
    public static com.google.protobuf.Internal.EnumLiteMap<FormatEncoding>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<FormatEncoding>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<FormatEncoding>() {
            public FormatEncoding findValueByNumber(int number) {
              return FormatEncoding.valueOf(number);
            }
          };
    
    public final com.google.protobuf.Descriptors.EnumValueDescriptor
        getValueDescriptor() {
      return getDescriptor().getValues().get(index);
    }
    public final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptorForType() {
      return getDescriptor();
    }
    public static final com.google.protobuf.Descriptors.EnumDescriptor
        getDescriptor() {
      return LCFormatEncoding.getDescriptor().getEnumTypes().get(0);
    }
    
    private static final FormatEncoding[] VALUES = {
      picture_jpeg, picture_tif, audio_mp3, audio_wav, video_wmv, 
    };
    
    public static FormatEncoding valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }
    
    private final int index;
    private final int value;
    
    private FormatEncoding(int index, int value) {
      this.index = index;
      this.value = value;
    }
    
    // @@protoc_insertion_point(enum_scope:FormatEncoding)
  }
  
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n4core/proto/terminal/common/java/Format" +
      "Encoding.proto*`\n\016FormatEncoding\022\020\n\014pict" +
      "ure_jpeg\020\000\022\017\n\013picture_tif\020\001\022\r\n\taudio_mp3" +
      "\020\002\022\r\n\taudio_wav\020\003\022\r\n\tvideo_wmv\020\004BR\n>com." +
      "navinfo.opentsp.platform.location.protoc" +
      "ol.terminal.commonB\020LCFormatEncoding"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}