// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/terminal/query/travelrecorder/TRCollectParaModifyRecordRes.proto

package com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder;

public final class LCTRCollectParaModifyRecordRes {
  private LCTRCollectParaModifyRecordRes() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface TRCollectParaModifyRecordResOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int32 serialNumber = 1;
    boolean hasSerialNumber();
    int getSerialNumber();
    
    // required .ResponseResult result = 2;
    boolean hasResult();
    com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult getResult();
    
    // repeated .ModifyRecord records = 3;
    java.util.List<com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord> 
        getRecordsList();
    com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord getRecords(int index);
    int getRecordsCount();
    java.util.List<? extends com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecordOrBuilder> 
        getRecordsOrBuilderList();
    com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecordOrBuilder getRecordsOrBuilder(
        int index);
  }
  public static final class TRCollectParaModifyRecordRes extends
      com.google.protobuf.GeneratedMessage
      implements TRCollectParaModifyRecordResOrBuilder {
    // Use TRCollectParaModifyRecordRes.newBuilder() to construct.
    private TRCollectParaModifyRecordRes(Builder builder) {
      super(builder);
    }
    private TRCollectParaModifyRecordRes(boolean noInit) {}
    
    private static final TRCollectParaModifyRecordRes defaultInstance;
    public static TRCollectParaModifyRecordRes getDefaultInstance() {
      return defaultInstance;
    }
    
    public TRCollectParaModifyRecordRes getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.internal_static_TRCollectParaModifyRecordRes_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.internal_static_TRCollectParaModifyRecordRes_fieldAccessorTable;
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
    
    // repeated .ModifyRecord records = 3;
    public static final int RECORDS_FIELD_NUMBER = 3;
    private java.util.List<com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord> records_;
    public java.util.List<com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord> getRecordsList() {
      return records_;
    }
    public java.util.List<? extends com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecordOrBuilder> 
        getRecordsOrBuilderList() {
      return records_;
    }
    public int getRecordsCount() {
      return records_.size();
    }
    public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord getRecords(int index) {
      return records_.get(index);
    }
    public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecordOrBuilder getRecordsOrBuilder(
        int index) {
      return records_.get(index);
    }
    
    private void initFields() {
      serialNumber_ = 0;
      result_ = com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.ResponseResult.success;
      records_ = java.util.Collections.emptyList();
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
      for (int i = 0; i < getRecordsCount(); i++) {
        if (!getRecords(i).isInitialized()) {
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
      for (int i = 0; i < records_.size(); i++) {
        output.writeMessage(3, records_.get(i));
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
      for (int i = 0; i < records_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, records_.get(i));
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
    
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes parseDelimitedFrom(
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
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes prototype) {
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
       implements com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordResOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.internal_static_TRCollectParaModifyRecordRes_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.internal_static_TRCollectParaModifyRecordRes_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getRecordsFieldBuilder();
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
        if (recordsBuilder_ == null) {
          records_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000004);
        } else {
          recordsBuilder_.clear();
        }
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes.getDescriptor();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes getDefaultInstanceForType() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes.getDefaultInstance();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes build() {
        com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes buildPartial() {
        com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes result = new com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes(this);
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
        if (recordsBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004)) {
            records_ = java.util.Collections.unmodifiableList(records_);
            bitField0_ = (bitField0_ & ~0x00000004);
          }
          result.records_ = records_;
        } else {
          result.records_ = recordsBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes) {
          return mergeFrom((com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes other) {
        if (other == com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes.getDefaultInstance()) return this;
        if (other.hasSerialNumber()) {
          setSerialNumber(other.getSerialNumber());
        }
        if (other.hasResult()) {
          setResult(other.getResult());
        }
        if (recordsBuilder_ == null) {
          if (!other.records_.isEmpty()) {
            if (records_.isEmpty()) {
              records_ = other.records_;
              bitField0_ = (bitField0_ & ~0x00000004);
            } else {
              ensureRecordsIsMutable();
              records_.addAll(other.records_);
            }
            onChanged();
          }
        } else {
          if (!other.records_.isEmpty()) {
            if (recordsBuilder_.isEmpty()) {
              recordsBuilder_.dispose();
              recordsBuilder_ = null;
              records_ = other.records_;
              bitField0_ = (bitField0_ & ~0x00000004);
              recordsBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getRecordsFieldBuilder() : null;
            } else {
              recordsBuilder_.addAllMessages(other.records_);
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
        for (int i = 0; i < getRecordsCount(); i++) {
          if (!getRecords(i).isInitialized()) {
            
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
            case 26: {
              com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord.Builder subBuilder = com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addRecords(subBuilder.buildPartial());
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
      
      // repeated .ModifyRecord records = 3;
      private java.util.List<com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord> records_ =
        java.util.Collections.emptyList();
      private void ensureRecordsIsMutable() {
        if (!((bitField0_ & 0x00000004) == 0x00000004)) {
          records_ = new java.util.ArrayList<com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord>(records_);
          bitField0_ |= 0x00000004;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord, com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord.Builder, com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecordOrBuilder> recordsBuilder_;
      
      public java.util.List<com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord> getRecordsList() {
        if (recordsBuilder_ == null) {
          return java.util.Collections.unmodifiableList(records_);
        } else {
          return recordsBuilder_.getMessageList();
        }
      }
      public int getRecordsCount() {
        if (recordsBuilder_ == null) {
          return records_.size();
        } else {
          return recordsBuilder_.getCount();
        }
      }
      public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord getRecords(int index) {
        if (recordsBuilder_ == null) {
          return records_.get(index);
        } else {
          return recordsBuilder_.getMessage(index);
        }
      }
      public Builder setRecords(
          int index, com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord value) {
        if (recordsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureRecordsIsMutable();
          records_.set(index, value);
          onChanged();
        } else {
          recordsBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setRecords(
          int index, com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord.Builder builderForValue) {
        if (recordsBuilder_ == null) {
          ensureRecordsIsMutable();
          records_.set(index, builderForValue.build());
          onChanged();
        } else {
          recordsBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addRecords(com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord value) {
        if (recordsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureRecordsIsMutable();
          records_.add(value);
          onChanged();
        } else {
          recordsBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addRecords(
          int index, com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord value) {
        if (recordsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureRecordsIsMutable();
          records_.add(index, value);
          onChanged();
        } else {
          recordsBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addRecords(
          com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord.Builder builderForValue) {
        if (recordsBuilder_ == null) {
          ensureRecordsIsMutable();
          records_.add(builderForValue.build());
          onChanged();
        } else {
          recordsBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addRecords(
          int index, com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord.Builder builderForValue) {
        if (recordsBuilder_ == null) {
          ensureRecordsIsMutable();
          records_.add(index, builderForValue.build());
          onChanged();
        } else {
          recordsBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllRecords(
          java.lang.Iterable<? extends com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord> values) {
        if (recordsBuilder_ == null) {
          ensureRecordsIsMutable();
          super.addAll(values, records_);
          onChanged();
        } else {
          recordsBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearRecords() {
        if (recordsBuilder_ == null) {
          records_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000004);
          onChanged();
        } else {
          recordsBuilder_.clear();
        }
        return this;
      }
      public Builder removeRecords(int index) {
        if (recordsBuilder_ == null) {
          ensureRecordsIsMutable();
          records_.remove(index);
          onChanged();
        } else {
          recordsBuilder_.remove(index);
        }
        return this;
      }
      public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord.Builder getRecordsBuilder(
          int index) {
        return getRecordsFieldBuilder().getBuilder(index);
      }
      public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecordOrBuilder getRecordsOrBuilder(
          int index) {
        if (recordsBuilder_ == null) {
          return records_.get(index);  } else {
          return recordsBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecordOrBuilder> 
           getRecordsOrBuilderList() {
        if (recordsBuilder_ != null) {
          return recordsBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(records_);
        }
      }
      public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord.Builder addRecordsBuilder() {
        return getRecordsFieldBuilder().addBuilder(
            com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord.getDefaultInstance());
      }
      public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord.Builder addRecordsBuilder(
          int index) {
        return getRecordsFieldBuilder().addBuilder(
            index, com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord.getDefaultInstance());
      }
      public java.util.List<com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord.Builder> 
           getRecordsBuilderList() {
        return getRecordsFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord, com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord.Builder, com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecordOrBuilder> 
          getRecordsFieldBuilder() {
        if (recordsBuilder_ == null) {
          recordsBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord, com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecord.Builder, com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.ModifyRecordOrBuilder>(
                  records_,
                  ((bitField0_ & 0x00000004) == 0x00000004),
                  getParentForChildren(),
                  isClean());
          records_ = null;
        }
        return recordsBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:TRCollectParaModifyRecordRes)
    }
    
    static {
      defaultInstance = new TRCollectParaModifyRecordRes(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:TRCollectParaModifyRecordRes)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_TRCollectParaModifyRecordRes_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_TRCollectParaModifyRecordRes_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\nKcore/proto/terminal/query/travelrecord" +
      "er/TRCollectParaModifyRecordRes.proto\032+c" +
      "ore/proto/common/java/ResponseResult.pro" +
      "to\032Fcore/proto/terminal/query/travelreco" +
      "rder/block/java/ModifyRecord.proto\"u\n\034TR" +
      "CollectParaModifyRecordRes\022\024\n\014serialNumb" +
      "er\030\001 \002(\005\022\037\n\006result\030\002 \002(\0162\017.ResponseResul" +
      "t\022\036\n\007records\030\003 \003(\0132\r.ModifyRecordBn\nLcom" +
      ".navinfo.opentsp.platform.location.proto" +
      "col.terminal.query.travelrecorderB\036LCTRC",
      "ollectParaModifyRecordRes"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_TRCollectParaModifyRecordRes_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_TRCollectParaModifyRecordRes_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_TRCollectParaModifyRecordRes_descriptor,
              new java.lang.String[] { "SerialNumber", "Result", "Records", },
              com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes.class,
              com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRCollectParaModifyRecordRes.TRCollectParaModifyRecordRes.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult.getDescriptor(),
          com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCModifyRecord.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
