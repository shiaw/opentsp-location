// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/terminal/common/java/PhotoResult.proto

package com.navinfo.opentsp.platform.location.protocol.terminal.common;

public final class LCPhotoResult {
  private LCPhotoResult() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public enum PhotoResult
      implements com.google.protobuf.ProtocolMessageEnum {
    success1(0, 0),
    failed(1, 1),
    channelNotSupport(2, 2),
    ;
    
    public static final int success1_VALUE = 0;
    public static final int failed_VALUE = 1;
    public static final int channelNotSupport_VALUE = 2;
    
    
    public final int getNumber() { return value; }
    
    public static PhotoResult valueOf(int value) {
      switch (value) {
        case 0: return success1;
        case 1: return failed;
        case 2: return channelNotSupport;
        default: return null;
      }
    }
    
    public static com.google.protobuf.Internal.EnumLiteMap<PhotoResult>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<PhotoResult>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<PhotoResult>() {
            public PhotoResult findValueByNumber(int number) {
              return PhotoResult.valueOf(number);
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
      return LCPhotoResult.getDescriptor().getEnumTypes().get(0);
    }
    
    private static final PhotoResult[] VALUES = {
      success1, failed, channelNotSupport, 
    };
    
    public static PhotoResult valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }
    
    private final int index;
    private final int value;
    
    private PhotoResult(int index, int value) {
      this.index = index;
      this.value = value;
    }
    
    // @@protoc_insertion_point(enum_scope:PhotoResult)
  }
  
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n1core/proto/terminal/common/java/PhotoR" +
      "esult.proto*>\n\013PhotoResult\022\014\n\010success1\020\000" +
      "\022\n\n\006failed\020\001\022\025\n\021channelNotSupport\020\002BO\n>c" +
      "om.navinfo.opentsp.platform.location.pro" +
      "tocol.terminal.commonB\rLCPhotoResult"
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