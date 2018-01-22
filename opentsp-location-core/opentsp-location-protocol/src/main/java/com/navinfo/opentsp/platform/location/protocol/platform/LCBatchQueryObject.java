// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/platform/java/BatchQueryObject.proto

package com.navinfo.opentsp.platform.location.protocol.platform;

public final class LCBatchQueryObject {
  private LCBatchQueryObject() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface BatchQueryObjectOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required .DistrictCode districtCode = 1;
    boolean hasDistrictCode();
    com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode getDistrictCode();
    
    // repeated int64 terminalIdentify = 2;
    java.util.List<Long> getTerminalIdentifyList();
    int getTerminalIdentifyCount();
    long getTerminalIdentify(int index);
  }
  public static final class BatchQueryObject extends
      com.google.protobuf.GeneratedMessage
      implements BatchQueryObjectOrBuilder {
    // Use BatchQueryObject.newBuilder() to construct.
    private BatchQueryObject(Builder builder) {
      super(builder);
    }
    private BatchQueryObject(boolean noInit) {}
    
    private static final BatchQueryObject defaultInstance;
    public static BatchQueryObject getDefaultInstance() {
      return defaultInstance;
    }
    
    public BatchQueryObject getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return LCBatchQueryObject.internal_static_BatchQueryObject_descriptor;
    }
    
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return LCBatchQueryObject.internal_static_BatchQueryObject_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required .DistrictCode districtCode = 1;
    public static final int DISTRICTCODE_FIELD_NUMBER = 1;
    private com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode districtCode_;
    public boolean hasDistrictCode() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode getDistrictCode() {
      return districtCode_;
    }
    
    // repeated int64 terminalIdentify = 2;
    public static final int TERMINALIDENTIFY_FIELD_NUMBER = 2;
    private java.util.List<Long> terminalIdentify_;
    public java.util.List<Long>
        getTerminalIdentifyList() {
      return terminalIdentify_;
    }
    public int getTerminalIdentifyCount() {
      return terminalIdentify_.size();
    }
    public long getTerminalIdentify(int index) {
      return terminalIdentify_.get(index);
    }
    
    private void initFields() {
      districtCode_ = com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode.none;
      terminalIdentify_ = java.util.Collections.emptyList();;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasDistrictCode()) {
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
        output.writeEnum(1, districtCode_.getNumber());
      }
      for (int i = 0; i < terminalIdentify_.size(); i++) {
        output.writeInt64(2, terminalIdentify_.get(i));
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
          .computeEnumSize(1, districtCode_.getNumber());
      }
      {
        int dataSize = 0;
        for (int i = 0; i < terminalIdentify_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeInt64SizeNoTag(terminalIdentify_.get(i));
        }
        size += dataSize;
        size += 1 * getTerminalIdentifyList().size();
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    private static final long serialVersionUID = 0L;
    @Override
    protected Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }
    
    public static BatchQueryObject parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static BatchQueryObject parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static BatchQueryObject parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static BatchQueryObject parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static BatchQueryObject parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static BatchQueryObject parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static BatchQueryObject parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static BatchQueryObject parseDelimitedFrom(
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
    public static BatchQueryObject parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static BatchQueryObject parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(BatchQueryObject prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    @Override
    protected Builder newBuilderForType(
        BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements BatchQueryObjectOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return LCBatchQueryObject.internal_static_BatchQueryObject_descriptor;
      }
      
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return LCBatchQueryObject.internal_static_BatchQueryObject_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.platform.LCBatchQueryObject.BatchQueryObject.newBuilder()
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
        districtCode_ = com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode.none;
        bitField0_ = (bitField0_ & ~0x00000001);
        terminalIdentify_ = java.util.Collections.emptyList();;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return BatchQueryObject.getDescriptor();
      }
      
      public BatchQueryObject getDefaultInstanceForType() {
        return BatchQueryObject.getDefaultInstance();
      }
      
      public BatchQueryObject build() {
        BatchQueryObject result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private BatchQueryObject buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        BatchQueryObject result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public BatchQueryObject buildPartial() {
        BatchQueryObject result = new BatchQueryObject(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.districtCode_ = districtCode_;
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          terminalIdentify_ = java.util.Collections.unmodifiableList(terminalIdentify_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.terminalIdentify_ = terminalIdentify_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof BatchQueryObject) {
          return mergeFrom((BatchQueryObject)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(BatchQueryObject other) {
        if (other == BatchQueryObject.getDefaultInstance()) return this;
        if (other.hasDistrictCode()) {
          setDistrictCode(other.getDistrictCode());
        }
        if (!other.terminalIdentify_.isEmpty()) {
          if (terminalIdentify_.isEmpty()) {
            terminalIdentify_ = other.terminalIdentify_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureTerminalIdentifyIsMutable();
            terminalIdentify_.addAll(other.terminalIdentify_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasDistrictCode()) {
          
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
              int rawValue = input.readEnum();
              com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode value = com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(1, rawValue);
              } else {
                bitField0_ |= 0x00000001;
                districtCode_ = value;
              }
              break;
            }
            case 16: {
              ensureTerminalIdentifyIsMutable();
              terminalIdentify_.add(input.readInt64());
              break;
            }
            case 18: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              while (input.getBytesUntilLimit() > 0) {
                addTerminalIdentify(input.readInt64());
              }
              input.popLimit(limit);
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required .DistrictCode districtCode = 1;
      private com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode districtCode_ = com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode.none;
      public boolean hasDistrictCode() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode getDistrictCode() {
        return districtCode_;
      }
      public Builder setDistrictCode(com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000001;
        districtCode_ = value;
        onChanged();
        return this;
      }
      public Builder clearDistrictCode() {
        bitField0_ = (bitField0_ & ~0x00000001);
        districtCode_ = com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.DistrictCode.none;
        onChanged();
        return this;
      }
      
      // repeated int64 terminalIdentify = 2;
      private java.util.List<Long> terminalIdentify_ = java.util.Collections.emptyList();;
      private void ensureTerminalIdentifyIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          terminalIdentify_ = new java.util.ArrayList<Long>(terminalIdentify_);
          bitField0_ |= 0x00000002;
         }
      }
      public java.util.List<Long>
          getTerminalIdentifyList() {
        return java.util.Collections.unmodifiableList(terminalIdentify_);
      }
      public int getTerminalIdentifyCount() {
        return terminalIdentify_.size();
      }
      public long getTerminalIdentify(int index) {
        return terminalIdentify_.get(index);
      }
      public Builder setTerminalIdentify(
          int index, long value) {
        ensureTerminalIdentifyIsMutable();
        terminalIdentify_.set(index, value);
        onChanged();
        return this;
      }
      public Builder addTerminalIdentify(long value) {
        ensureTerminalIdentifyIsMutable();
        terminalIdentify_.add(value);
        onChanged();
        return this;
      }
      public Builder addAllTerminalIdentify(
          Iterable<? extends Long> values) {
        ensureTerminalIdentifyIsMutable();
        super.addAll(values, terminalIdentify_);
        onChanged();
        return this;
      }
      public Builder clearTerminalIdentify() {
        terminalIdentify_ = java.util.Collections.emptyList();;
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:BatchQueryObject)
    }
    
    static {
      defaultInstance = new BatchQueryObject(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:BatchQueryObject)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_BatchQueryObject_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_BatchQueryObject_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n/core/proto/platform/java/BatchQueryObj" +
      "ect.proto\032)core/proto/common/java/Distri" +
      "ctCode.proto\"Q\n\020BatchQueryObject\022#\n\014dist" +
      "rictCode\030\001 \002(\0162\r.DistrictCode\022\030\n\020termina" +
      "lIdentify\030\002 \003(\003BM\n7com.navinfo.opentsp.p" +
      "latform.location.protocol.platformB\022LCBa" +
      "tchQueryObject"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_BatchQueryObject_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_BatchQueryObject_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_BatchQueryObject_descriptor,
              new String[] { "DistrictCode", "TerminalIdentify", },
              BatchQueryObject.class,
              BatchQueryObject.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.navinfo.opentsp.platform.location.protocol.common.LCDistrictCode.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}