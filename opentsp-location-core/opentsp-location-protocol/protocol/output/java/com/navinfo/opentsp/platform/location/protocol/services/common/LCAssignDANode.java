// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/services/common/java/AssignDANode.proto

package com.navinfo.opentsp.platform.location.protocol.services.common;

public final class LCAssignDANode {
  private LCAssignDANode() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface AssignDANodeOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required string daIP = 1;
    boolean hasDaIP();
    String getDaIP();
    
    // required int32 daPort = 2;
    boolean hasDaPort();
    int getDaPort();
  }
  public static final class AssignDANode extends
      com.google.protobuf.GeneratedMessage
      implements AssignDANodeOrBuilder {
    // Use AssignDANode.newBuilder() to construct.
    private AssignDANode(Builder builder) {
      super(builder);
    }
    private AssignDANode(boolean noInit) {}
    
    private static final AssignDANode defaultInstance;
    public static AssignDANode getDefaultInstance() {
      return defaultInstance;
    }
    
    public AssignDANode getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.internal_static_AssignDANode_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.internal_static_AssignDANode_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required string daIP = 1;
    public static final int DAIP_FIELD_NUMBER = 1;
    private java.lang.Object daIP_;
    public boolean hasDaIP() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public String getDaIP() {
      java.lang.Object ref = daIP_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          daIP_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getDaIPBytes() {
      java.lang.Object ref = daIP_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        daIP_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // required int32 daPort = 2;
    public static final int DAPORT_FIELD_NUMBER = 2;
    private int daPort_;
    public boolean hasDaPort() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public int getDaPort() {
      return daPort_;
    }
    
    private void initFields() {
      daIP_ = "";
      daPort_ = 0;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasDaIP()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasDaPort()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, getDaIPBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, daPort_);
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(1, getDaIPBytes());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, daPort_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }
    
    public static com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANodeOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.internal_static_AssignDANode_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.internal_static_AssignDANode_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        daIP_ = "";
        bitField0_ = (bitField0_ & ~0x00000001);
        daPort_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode.getDescriptor();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode getDefaultInstanceForType() {
        return com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode.getDefaultInstance();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode build() {
        com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode buildPartial() {
        com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode result = new com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.daIP_ = daIP_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.daPort_ = daPort_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode) {
          return mergeFrom((com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode other) {
        if (other == com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode.getDefaultInstance()) return this;
        if (other.hasDaIP()) {
          setDaIP(other.getDaIP());
        }
        if (other.hasDaPort()) {
          setDaPort(other.getDaPort());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasDaIP()) {
          
          return false;
        }
        if (!hasDaPort()) {
          
          return false;
        }
        return true;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                onChanged();
                return this;
              }
              break;
            }
            case 10: {
              bitField0_ |= 0x00000001;
              daIP_ = input.readBytes();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              daPort_ = input.readInt32();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required string daIP = 1;
      private java.lang.Object daIP_ = "";
      public boolean hasDaIP() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public String getDaIP() {
        java.lang.Object ref = daIP_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          daIP_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setDaIP(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        daIP_ = value;
        onChanged();
        return this;
      }
      public Builder clearDaIP() {
        bitField0_ = (bitField0_ & ~0x00000001);
        daIP_ = getDefaultInstance().getDaIP();
        onChanged();
        return this;
      }
      void setDaIP(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000001;
        daIP_ = value;
        onChanged();
      }
      
      // required int32 daPort = 2;
      private int daPort_ ;
      public boolean hasDaPort() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public int getDaPort() {
        return daPort_;
      }
      public Builder setDaPort(int value) {
        bitField0_ |= 0x00000002;
        daPort_ = value;
        onChanged();
        return this;
      }
      public Builder clearDaPort() {
        bitField0_ = (bitField0_ & ~0x00000002);
        daPort_ = 0;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:AssignDANode)
    }
    
    static {
      defaultInstance = new AssignDANode(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:AssignDANode)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_AssignDANode_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_AssignDANode_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n2core/proto/services/common/java/Assign" +
      "DANode.proto\",\n\014AssignDANode\022\014\n\004daIP\030\001 \002" +
      "(\t\022\016\n\006daPort\030\002 \002(\005BP\n>com.navinfo.opents" +
      "p.platform.location.protocol.services.co" +
      "mmonB\016LCAssignDANode"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_AssignDANode_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_AssignDANode_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_AssignDANode_descriptor,
              new java.lang.String[] { "DaIP", "DaPort", },
              com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode.class,
              com.navinfo.opentsp.platform.location.protocol.services.common.LCAssignDANode.AssignDANode.Builder.class);
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
