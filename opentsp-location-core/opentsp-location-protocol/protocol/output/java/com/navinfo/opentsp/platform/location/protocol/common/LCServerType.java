// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/common/java/ServerType.proto

package com.navinfo.opentsp.platform.location.protocol.common;

public final class LCServerType {
  private LCServerType() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public enum ServerType
      implements com.google.protobuf.ProtocolMessageEnum {
    businessServer(0, 1),
    cloudNode(1, 2),
    ;
    
    public static final int businessServer_VALUE = 1;
    public static final int cloudNode_VALUE = 2;
    
    
    public final int getNumber() { return value; }
    
    public static ServerType valueOf(int value) {
      switch (value) {
        case 1: return businessServer;
        case 2: return cloudNode;
        default: return null;
      }
    }
    
    public static com.google.protobuf.Internal.EnumLiteMap<ServerType>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<ServerType>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<ServerType>() {
            public ServerType findValueByNumber(int number) {
              return ServerType.valueOf(number);
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
      return com.navinfo.opentsp.platform.location.protocol.common.LCServerType.getDescriptor().getEnumTypes().get(0);
    }
    
    private static final ServerType[] VALUES = {
      businessServer, cloudNode, 
    };
    
    public static ServerType valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new java.lang.IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }
    
    private final int index;
    private final int value;
    
    private ServerType(int index, int value) {
      this.index = index;
      this.value = value;
    }
    
    // @@protoc_insertion_point(enum_scope:ServerType)
  }
  
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\'core/proto/common/java/ServerType.prot" +
      "o*/\n\nServerType\022\022\n\016businessServer\020\001\022\r\n\tc" +
      "loudNode\020\002BE\n5com.navinfo.opentsp.platfo" +
      "rm.location.protocol.commonB\014LCServerTyp" +
      "e"
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
