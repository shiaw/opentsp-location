// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/terminal/monitor/java/TakePhotographyRes.proto

package com.navinfo.opentsp.platform.location.protocol.terminal.monitor;

public final class LCTakePhotographyRes {
  private LCTakePhotographyRes() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface TakePhotographyResOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int32 serialNumber = 1;
    boolean hasSerialNumber();
    int getSerialNumber();
    
    // required .ResponseResult result = 2;
    boolean hasResult();
    com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult getResult();
    
    // required .PhotoResult results = 3;
    boolean hasResults();
    com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPhotoResult.PhotoResult getResults();
    
    // repeated int64 mediaIdentify = 4;
    java.util.List<java.lang.Long> getMediaIdentifyList();
    int getMediaIdentifyCount();
    long getMediaIdentify(int index);
  }
  public static final class TakePhotographyRes extends
      com.google.protobuf.GeneratedMessage
      implements TakePhotographyResOrBuilder {
    // Use TakePhotographyRes.newBuilder() to construct.
    private TakePhotographyRes(Builder builder) {
      super(builder);
    }
    private TakePhotographyRes(boolean noInit) {}
    
    private static final TakePhotographyRes defaultInstance;
    public static TakePhotographyRes getDefaultInstance() {
      return defaultInstance;
    }
    
    public TakePhotographyRes getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.internal_static_TakePhotographyRes_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.internal_static_TakePhotographyRes_fieldAccessorTable;
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
    
    // required .ResponseResult result = 2;
    public static final int RESULT_FIELD_NUMBER = 2;
    private com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult result_;
    public boolean hasResult() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult getResult() {
      return result_;
    }
    
    // required .PhotoResult results = 3;
    public static final int RESULTS_FIELD_NUMBER = 3;
    private com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPhotoResult.PhotoResult results_;
    public boolean hasResults() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPhotoResult.PhotoResult getResults() {
      return results_;
    }
    
    // repeated int64 mediaIdentify = 4;
    public static final int MEDIAIDENTIFY_FIELD_NUMBER = 4;
    private java.util.List<java.lang.Long> mediaIdentify_;
    public java.util.List<java.lang.Long>
        getMediaIdentifyList() {
      return mediaIdentify_;
    }
    public int getMediaIdentifyCount() {
      return mediaIdentify_.size();
    }
    public long getMediaIdentify(int index) {
      return mediaIdentify_.get(index);
    }
    
    private void initFields() {
      serialNumber_ = 0;
      result_ = com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult.success;
      results_ = com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPhotoResult.PhotoResult.success1;
      mediaIdentify_ = java.util.Collections.emptyList();;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasSerialNumber()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasResult()) {
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
        output.writeEnum(2, result_.getNumber());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeEnum(3, results_.getNumber());
      }
      for (int i = 0; i < mediaIdentify_.size(); i++) {
        output.writeInt64(4, mediaIdentify_.get(i));
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
          .computeEnumSize(2, result_.getNumber());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(3, results_.getNumber());
      }
      {
        int dataSize = 0;
        for (int i = 0; i < mediaIdentify_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeInt64SizeNoTag(mediaIdentify_.get(i));
        }
        size += dataSize;
        size += 1 * getMediaIdentifyList().size();
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
    
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes parseDelimitedFrom(
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
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes prototype) {
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
       implements com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyResOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.internal_static_TakePhotographyRes_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.internal_static_TakePhotographyRes_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes.newBuilder()
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
        result_ = com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult.success;
        bitField0_ = (bitField0_ & ~0x00000002);
        results_ = com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPhotoResult.PhotoResult.success1;
        bitField0_ = (bitField0_ & ~0x00000004);
        mediaIdentify_ = java.util.Collections.emptyList();;
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes.getDescriptor();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes getDefaultInstanceForType() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes.getDefaultInstance();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes build() {
        com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes buildPartial() {
        com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes result = new com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.serialNumber_ = serialNumber_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.result_ = result_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.results_ = results_;
        if (((bitField0_ & 0x00000008) == 0x00000008)) {
          mediaIdentify_ = java.util.Collections.unmodifiableList(mediaIdentify_);
          bitField0_ = (bitField0_ & ~0x00000008);
        }
        result.mediaIdentify_ = mediaIdentify_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes) {
          return mergeFrom((com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes other) {
        if (other == com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes.getDefaultInstance()) return this;
        if (other.hasSerialNumber()) {
          setSerialNumber(other.getSerialNumber());
        }
        if (other.hasResult()) {
          setResult(other.getResult());
        }
        if (other.hasResults()) {
          setResults(other.getResults());
        }
        if (!other.mediaIdentify_.isEmpty()) {
          if (mediaIdentify_.isEmpty()) {
            mediaIdentify_ = other.mediaIdentify_;
            bitField0_ = (bitField0_ & ~0x00000008);
          } else {
            ensureMediaIdentifyIsMutable();
            mediaIdentify_.addAll(other.mediaIdentify_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasSerialNumber()) {
          
          return false;
        }
        if (!hasResult()) {
          
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
              com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult value = com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(2, rawValue);
              } else {
                bitField0_ |= 0x00000002;
                result_ = value;
              }
              break;
            }
            case 24: {
              int rawValue = input.readEnum();
              com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPhotoResult.PhotoResult value = com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPhotoResult.PhotoResult.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(3, rawValue);
              } else {
                bitField0_ |= 0x00000004;
                results_ = value;
              }
              break;
            }
            case 32: {
              ensureMediaIdentifyIsMutable();
              mediaIdentify_.add(input.readInt64());
              break;
            }
            case 34: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              while (input.getBytesUntilLimit() > 0) {
                addMediaIdentify(input.readInt64());
              }
              input.popLimit(limit);
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
      
      // required .ResponseResult result = 2;
      private com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult result_ = com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult.success;
      public boolean hasResult() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult getResult() {
        return result_;
      }
      public Builder setResult(com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000002;
        result_ = value;
        onChanged();
        return this;
      }
      public Builder clearResult() {
        bitField0_ = (bitField0_ & ~0x00000002);
        result_ = com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult.success;
        onChanged();
        return this;
      }
      
      // required .PhotoResult results = 3;
      private com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPhotoResult.PhotoResult results_ = com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPhotoResult.PhotoResult.success1;
      public boolean hasResults() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPhotoResult.PhotoResult getResults() {
        return results_;
      }
      public Builder setResults(com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPhotoResult.PhotoResult value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000004;
        results_ = value;
        onChanged();
        return this;
      }
      public Builder clearResults() {
        bitField0_ = (bitField0_ & ~0x00000004);
        results_ = com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPhotoResult.PhotoResult.success1;
        onChanged();
        return this;
      }
      
      // repeated int64 mediaIdentify = 4;
      private java.util.List<java.lang.Long> mediaIdentify_ = java.util.Collections.emptyList();;
      private void ensureMediaIdentifyIsMutable() {
        if (!((bitField0_ & 0x00000008) == 0x00000008)) {
          mediaIdentify_ = new java.util.ArrayList<java.lang.Long>(mediaIdentify_);
          bitField0_ |= 0x00000008;
         }
      }
      public java.util.List<java.lang.Long>
          getMediaIdentifyList() {
        return java.util.Collections.unmodifiableList(mediaIdentify_);
      }
      public int getMediaIdentifyCount() {
        return mediaIdentify_.size();
      }
      public long getMediaIdentify(int index) {
        return mediaIdentify_.get(index);
      }
      public Builder setMediaIdentify(
          int index, long value) {
        ensureMediaIdentifyIsMutable();
        mediaIdentify_.set(index, value);
        onChanged();
        return this;
      }
      public Builder addMediaIdentify(long value) {
        ensureMediaIdentifyIsMutable();
        mediaIdentify_.add(value);
        onChanged();
        return this;
      }
      public Builder addAllMediaIdentify(
          java.lang.Iterable<? extends java.lang.Long> values) {
        ensureMediaIdentifyIsMutable();
        super.addAll(values, mediaIdentify_);
        onChanged();
        return this;
      }
      public Builder clearMediaIdentify() {
        mediaIdentify_ = java.util.Collections.emptyList();;
        bitField0_ = (bitField0_ & ~0x00000008);
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:TakePhotographyRes)
    }
    
    static {
      defaultInstance = new TakePhotographyRes(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:TakePhotographyRes)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_TakePhotographyRes_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_TakePhotographyRes_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n9core/proto/terminal/monitor/java/TakeP" +
      "hotographyRes.proto\032+core/proto/common/j" +
      "ava/ResponseResult.proto\0321core/proto/ter" +
      "minal/common/java/PhotoResult.proto\"\201\001\n\022" +
      "TakePhotographyRes\022\024\n\014serialNumber\030\001 \002(\005" +
      "\022\037\n\006result\030\002 \002(\0162\017.ResponseResult\022\035\n\007res" +
      "ults\030\003 \002(\0162\014.PhotoResult\022\025\n\rmediaIdentif" +
      "y\030\004 \003(\003BW\n?com.navinfo.opentsp.platform." +
      "location.protocol.terminal.monitorB\024LCTa" +
      "kePhotographyRes"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_TakePhotographyRes_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_TakePhotographyRes_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_TakePhotographyRes_descriptor,
              new java.lang.String[] { "SerialNumber", "Result", "Results", "MediaIdentify", },
              com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes.class,
              com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes.TakePhotographyRes.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.getDescriptor(),
          com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPhotoResult.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
