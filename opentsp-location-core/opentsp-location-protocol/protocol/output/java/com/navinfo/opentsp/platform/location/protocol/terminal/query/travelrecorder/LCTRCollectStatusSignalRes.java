// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/terminal/query/travelrecorder/TRCollectStatusSignalRes.proto

package com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder;

public final class LCTRCollectStatusSignalRes {
  private LCTRCollectStatusSignalRes() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface TRCollectStatusSignalResOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int32 serialNumber = 1;
    boolean hasSerialNumber();
    int getSerialNumber();
    
    // required .ResponseResult result = 2;
    boolean hasResult();
    com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult getResult();
    
    // optional int64 currentDate = 3;
    boolean hasCurrentDate();
    long getCurrentDate();
    
    // repeated .SignalData datas = 4;
    java.util.List<com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData> 
        getDatasList();
    com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData getDatas(int index);
    int getDatasCount();
    java.util.List<? extends com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalDataOrBuilder> 
        getDatasOrBuilderList();
    com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalDataOrBuilder getDatasOrBuilder(
        int index);
  }
  public static final class TRCollectStatusSignalRes extends
      com.google.protobuf.GeneratedMessage
      implements TRCollectStatusSignalResOrBuilder {
    // Use TRCollectStatusSignalRes.newBuilder() to construct.
    private TRCollectStatusSignalRes(Builder builder) {
      super(builder);
    }
    private TRCollectStatusSignalRes(boolean noInit) {}
    
    private static final TRCollectStatusSignalRes defaultInstance;
    public static TRCollectStatusSignalRes getDefaultInstance() {
      return defaultInstance;
    }
    
    public TRCollectStatusSignalRes getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.internal_static_TRCollectStatusSignalRes_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.internal_static_TRCollectStatusSignalRes_fieldAccessorTable;
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
    
    // optional int64 currentDate = 3;
    public static final int CURRENTDATE_FIELD_NUMBER = 3;
    private long currentDate_;
    public boolean hasCurrentDate() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public long getCurrentDate() {
      return currentDate_;
    }
    
    // repeated .SignalData datas = 4;
    public static final int DATAS_FIELD_NUMBER = 4;
    private java.util.List<com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData> datas_;
    public java.util.List<com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData> getDatasList() {
      return datas_;
    }
    public java.util.List<? extends com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalDataOrBuilder> 
        getDatasOrBuilderList() {
      return datas_;
    }
    public int getDatasCount() {
      return datas_.size();
    }
    public com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData getDatas(int index) {
      return datas_.get(index);
    }
    public com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalDataOrBuilder getDatasOrBuilder(
        int index) {
      return datas_.get(index);
    }
    
    private void initFields() {
      serialNumber_ = 0;
      result_ = com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult.success;
      currentDate_ = 0L;
      datas_ = java.util.Collections.emptyList();
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
      for (int i = 0; i < getDatasCount(); i++) {
        if (!getDatas(i).isInitialized()) {
          memoizedIsInitialized = 0;
          return false;
        }
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
        output.writeInt64(3, currentDate_);
      }
      for (int i = 0; i < datas_.size(); i++) {
        output.writeMessage(4, datas_.get(i));
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
          .computeInt64Size(3, currentDate_);
      }
      for (int i = 0; i < datas_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, datas_.get(i));
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
    
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes parseDelimitedFrom(
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
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes prototype) {
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
       implements com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalResOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.internal_static_TRCollectStatusSignalRes_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.internal_static_TRCollectStatusSignalRes_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getDatasFieldBuilder();
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
        currentDate_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000004);
        if (datasBuilder_ == null) {
          datas_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000008);
        } else {
          datasBuilder_.clear();
        }
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes.getDescriptor();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes getDefaultInstanceForType() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes.getDefaultInstance();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes build() {
        com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes buildPartial() {
        com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes result = new com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes(this);
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
        result.currentDate_ = currentDate_;
        if (datasBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008)) {
            datas_ = java.util.Collections.unmodifiableList(datas_);
            bitField0_ = (bitField0_ & ~0x00000008);
          }
          result.datas_ = datas_;
        } else {
          result.datas_ = datasBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes) {
          return mergeFrom((com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes other) {
        if (other == com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes.getDefaultInstance()) return this;
        if (other.hasSerialNumber()) {
          setSerialNumber(other.getSerialNumber());
        }
        if (other.hasResult()) {
          setResult(other.getResult());
        }
        if (other.hasCurrentDate()) {
          setCurrentDate(other.getCurrentDate());
        }
        if (datasBuilder_ == null) {
          if (!other.datas_.isEmpty()) {
            if (datas_.isEmpty()) {
              datas_ = other.datas_;
              bitField0_ = (bitField0_ & ~0x00000008);
            } else {
              ensureDatasIsMutable();
              datas_.addAll(other.datas_);
            }
            onChanged();
          }
        } else {
          if (!other.datas_.isEmpty()) {
            if (datasBuilder_.isEmpty()) {
              datasBuilder_.dispose();
              datasBuilder_ = null;
              datas_ = other.datas_;
              bitField0_ = (bitField0_ & ~0x00000008);
              datasBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getDatasFieldBuilder() : null;
            } else {
              datasBuilder_.addAllMessages(other.datas_);
            }
          }
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
        for (int i = 0; i < getDatasCount(); i++) {
          if (!getDatas(i).isInitialized()) {
            
            return false;
          }
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
              bitField0_ |= 0x00000004;
              currentDate_ = input.readInt64();
              break;
            }
            case 34: {
              com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData.Builder subBuilder = com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addDatas(subBuilder.buildPartial());
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
      
      // optional int64 currentDate = 3;
      private long currentDate_ ;
      public boolean hasCurrentDate() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public long getCurrentDate() {
        return currentDate_;
      }
      public Builder setCurrentDate(long value) {
        bitField0_ |= 0x00000004;
        currentDate_ = value;
        onChanged();
        return this;
      }
      public Builder clearCurrentDate() {
        bitField0_ = (bitField0_ & ~0x00000004);
        currentDate_ = 0L;
        onChanged();
        return this;
      }
      
      // repeated .SignalData datas = 4;
      private java.util.List<com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData> datas_ =
        java.util.Collections.emptyList();
      private void ensureDatasIsMutable() {
        if (!((bitField0_ & 0x00000008) == 0x00000008)) {
          datas_ = new java.util.ArrayList<com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData>(datas_);
          bitField0_ |= 0x00000008;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData, com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData.Builder, com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalDataOrBuilder> datasBuilder_;
      
      public java.util.List<com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData> getDatasList() {
        if (datasBuilder_ == null) {
          return java.util.Collections.unmodifiableList(datas_);
        } else {
          return datasBuilder_.getMessageList();
        }
      }
      public int getDatasCount() {
        if (datasBuilder_ == null) {
          return datas_.size();
        } else {
          return datasBuilder_.getCount();
        }
      }
      public com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData getDatas(int index) {
        if (datasBuilder_ == null) {
          return datas_.get(index);
        } else {
          return datasBuilder_.getMessage(index);
        }
      }
      public Builder setDatas(
          int index, com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData value) {
        if (datasBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureDatasIsMutable();
          datas_.set(index, value);
          onChanged();
        } else {
          datasBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setDatas(
          int index, com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData.Builder builderForValue) {
        if (datasBuilder_ == null) {
          ensureDatasIsMutable();
          datas_.set(index, builderForValue.build());
          onChanged();
        } else {
          datasBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addDatas(com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData value) {
        if (datasBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureDatasIsMutable();
          datas_.add(value);
          onChanged();
        } else {
          datasBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addDatas(
          int index, com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData value) {
        if (datasBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureDatasIsMutable();
          datas_.add(index, value);
          onChanged();
        } else {
          datasBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addDatas(
          com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData.Builder builderForValue) {
        if (datasBuilder_ == null) {
          ensureDatasIsMutable();
          datas_.add(builderForValue.build());
          onChanged();
        } else {
          datasBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addDatas(
          int index, com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData.Builder builderForValue) {
        if (datasBuilder_ == null) {
          ensureDatasIsMutable();
          datas_.add(index, builderForValue.build());
          onChanged();
        } else {
          datasBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllDatas(
          java.lang.Iterable<? extends com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData> values) {
        if (datasBuilder_ == null) {
          ensureDatasIsMutable();
          super.addAll(values, datas_);
          onChanged();
        } else {
          datasBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearDatas() {
        if (datasBuilder_ == null) {
          datas_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000008);
          onChanged();
        } else {
          datasBuilder_.clear();
        }
        return this;
      }
      public Builder removeDatas(int index) {
        if (datasBuilder_ == null) {
          ensureDatasIsMutable();
          datas_.remove(index);
          onChanged();
        } else {
          datasBuilder_.remove(index);
        }
        return this;
      }
      public com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData.Builder getDatasBuilder(
          int index) {
        return getDatasFieldBuilder().getBuilder(index);
      }
      public com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalDataOrBuilder getDatasOrBuilder(
          int index) {
        if (datasBuilder_ == null) {
          return datas_.get(index);  } else {
          return datasBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalDataOrBuilder> 
           getDatasOrBuilderList() {
        if (datasBuilder_ != null) {
          return datasBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(datas_);
        }
      }
      public com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData.Builder addDatasBuilder() {
        return getDatasFieldBuilder().addBuilder(
            com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData.getDefaultInstance());
      }
      public com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData.Builder addDatasBuilder(
          int index) {
        return getDatasFieldBuilder().addBuilder(
            index, com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData.getDefaultInstance());
      }
      public java.util.List<com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData.Builder> 
           getDatasBuilderList() {
        return getDatasFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData, com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData.Builder, com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalDataOrBuilder> 
          getDatasFieldBuilder() {
        if (datasBuilder_ == null) {
          datasBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData, com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalData.Builder, com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.SignalDataOrBuilder>(
                  datas_,
                  ((bitField0_ & 0x00000008) == 0x00000008),
                  getParentForChildren(),
                  isClean());
          datas_ = null;
        }
        return datasBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:TRCollectStatusSignalRes)
    }
    
    static {
      defaultInstance = new TRCollectStatusSignalRes(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:TRCollectStatusSignalRes)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_TRCollectStatusSignalRes_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_TRCollectStatusSignalRes_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\nGcore/proto/terminal/query/travelrecord" +
      "er/TRCollectStatusSignalRes.proto\0320core/" +
      "proto/terminal/common/java/SignalData.pr" +
      "oto\032+core/proto/common/java/ResponseResu" +
      "lt.proto\"\202\001\n\030TRCollectStatusSignalRes\022\024\n" +
      "\014serialNumber\030\001 \002(\005\022\037\n\006result\030\002 \002(\0162\017.Re" +
      "sponseResult\022\023\n\013currentDate\030\003 \001(\003\022\032\n\005dat" +
      "as\030\004 \003(\0132\013.SignalDataBj\nLcom.navinfo.ope" +
      "ntsp.platform.location.protocol.terminal" +
      ".query.travelrecorderB\032LCTRCollectStatus",
      "SignalRes"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_TRCollectStatusSignalRes_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_TRCollectStatusSignalRes_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_TRCollectStatusSignalRes_descriptor,
              new java.lang.String[] { "SerialNumber", "Result", "CurrentDate", "Datas", },
              com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes.class,
              com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectStatusSignalRes.TRCollectStatusSignalRes.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSignalData.getDescriptor(),
          com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
