// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/nodecluster/java/AssignNewNode.proto

package com.navinfo.opentsp.platform.location.protocol.nodecluster;

public final class LCAssignNewNode {
  private LCAssignNewNode() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface AssignNewNodeOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int32 serialNumber = 1;
    boolean hasSerialNumber();
    int getSerialNumber();
    
    // required .PlatformResponseResult results = 2;
    boolean hasResults();
    com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult getResults();
    
    // optional int64 newNodeCode = 3;
    boolean hasNewNodeCode();
    long getNewNodeCode();
  }
  public static final class AssignNewNode extends
      com.google.protobuf.GeneratedMessage
      implements AssignNewNodeOrBuilder {
    // Use AssignNewNode.newBuilder() to construct.
    private AssignNewNode(Builder builder) {
      super(builder);
    }
    private AssignNewNode(boolean noInit) {}
    
    private static final AssignNewNode defaultInstance;
    public static AssignNewNode getDefaultInstance() {
      return defaultInstance;
    }
    
    public AssignNewNode getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.internal_static_AssignNewNode_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.internal_static_AssignNewNode_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required int32 serialNumber = 1;
    public static final int SERIALNUMBER_FIELD_NUMBER = 1;
    private int serialNumber_;
    public boolean hasSerialNumber() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public int getSerialNumber() {
      return serialNumber_;
    }
    
    // required .PlatformResponseResult results = 2;
    public static final int RESULTS_FIELD_NUMBER = 2;
    private com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult results_;
    public boolean hasResults() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult getResults() {
      return results_;
    }
    
    // optional int64 newNodeCode = 3;
    public static final int NEWNODECODE_FIELD_NUMBER = 3;
    private long newNodeCode_;
    public boolean hasNewNodeCode() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public long getNewNodeCode() {
      return newNodeCode_;
    }
    
    private void initFields() {
      serialNumber_ = 0;
      results_ = com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult.failure;
      newNodeCode_ = 0L;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasSerialNumber()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasResults()) {
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
        output.writeInt32(1, serialNumber_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeEnum(2, results_.getNumber());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt64(3, newNodeCode_);
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
          .computeInt32Size(1, serialNumber_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(2, results_.getNumber());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(3, newNodeCode_);
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
    
    public static com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode parseDelimitedFrom(
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
    public static com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode prototype) {
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
       implements com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNodeOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.internal_static_AssignNewNode_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.internal_static_AssignNewNode_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode.newBuilder()
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
        serialNumber_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        results_ = com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult.failure;
        bitField0_ = (bitField0_ & ~0x00000002);
        newNodeCode_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode.getDescriptor();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode getDefaultInstanceForType() {
        return com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode.getDefaultInstance();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode build() {
        com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode buildPartial() {
        com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode result = new com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.serialNumber_ = serialNumber_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.results_ = results_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.newNodeCode_ = newNodeCode_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode) {
          return mergeFrom((com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode other) {
        if (other == com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode.getDefaultInstance()) return this;
        if (other.hasSerialNumber()) {
          setSerialNumber(other.getSerialNumber());
        }
        if (other.hasResults()) {
          setResults(other.getResults());
        }
        if (other.hasNewNodeCode()) {
          setNewNodeCode(other.getNewNodeCode());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasSerialNumber()) {
          
          return false;
        }
        if (!hasResults()) {
          
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
            case 8: {
              bitField0_ |= 0x00000001;
              serialNumber_ = input.readInt32();
              break;
            }
            case 16: {
              int rawValue = input.readEnum();
              com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult value = com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(2, rawValue);
              } else {
                bitField0_ |= 0x00000002;
                results_ = value;
              }
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              newNodeCode_ = input.readInt64();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required int32 serialNumber = 1;
      private int serialNumber_ ;
      public boolean hasSerialNumber() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public int getSerialNumber() {
        return serialNumber_;
      }
      public Builder setSerialNumber(int value) {
        bitField0_ |= 0x00000001;
        serialNumber_ = value;
        onChanged();
        return this;
      }
      public Builder clearSerialNumber() {
        bitField0_ = (bitField0_ & ~0x00000001);
        serialNumber_ = 0;
        onChanged();
        return this;
      }
      
      // required .PlatformResponseResult results = 2;
      private com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult results_ = com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult.failure;
      public boolean hasResults() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult getResults() {
        return results_;
      }
      public Builder setResults(com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000002;
        results_ = value;
        onChanged();
        return this;
      }
      public Builder clearResults() {
        bitField0_ = (bitField0_ & ~0x00000002);
        results_ = com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult.failure;
        onChanged();
        return this;
      }
      
      // optional int64 newNodeCode = 3;
      private long newNodeCode_ ;
      public boolean hasNewNodeCode() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public long getNewNodeCode() {
        return newNodeCode_;
      }
      public Builder setNewNodeCode(long value) {
        bitField0_ |= 0x00000004;
        newNodeCode_ = value;
        onChanged();
        return this;
      }
      public Builder clearNewNodeCode() {
        bitField0_ = (bitField0_ & ~0x00000004);
        newNodeCode_ = 0L;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:AssignNewNode)
    }
    
    static {
      defaultInstance = new AssignNewNode(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:AssignNewNode)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_AssignNewNode_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_AssignNewNode_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n/core/proto/nodecluster/java/AssignNewN" +
      "ode.proto\0323core/proto/common/java/Platfo" +
      "rmResponseResult.proto\"d\n\rAssignNewNode\022" +
      "\024\n\014serialNumber\030\001 \002(\005\022(\n\007results\030\002 \002(\0162\027" +
      ".PlatformResponseResult\022\023\n\013newNodeCode\030\003" +
      " \001(\003BM\n:com.navinfo.opentsp.platform.loc" +
      "ation.protocol.nodeclusterB\017LCAssignNewN" +
      "ode"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_AssignNewNode_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_AssignNewNode_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_AssignNewNode_descriptor,
              new java.lang.String[] { "SerialNumber", "Results", "NewNodeCode", },
              com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode.class,
              com.navinfo.opentsp.platform.location.protocol.nodecluster.LCAssignNewNode.AssignNewNode.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
