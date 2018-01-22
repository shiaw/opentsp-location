// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: PoiDenseLocation.proto

package com.navinfo.opentsp.platform.location.protocol.common;

public final class PoiDenseLocationPb {
  private PoiDenseLocationPb() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface PoiDenseLocationOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int64 terminalId = 1;
    boolean hasTerminalId();
    long getTerminalId();
    
    // repeated .RealTimeData dataList = 2;
    java.util.List<RealTimeDataPb.RealTimeData>
        getDataListList();
    RealTimeDataPb.RealTimeData getDataList(int index);
    int getDataListCount();
    java.util.List<? extends RealTimeDataPb.RealTimeDataOrBuilder>
        getDataListOrBuilderList();
    RealTimeDataPb.RealTimeDataOrBuilder getDataListOrBuilder(
            int index);
  }
  public static final class PoiDenseLocation extends
      com.google.protobuf.GeneratedMessage
      implements PoiDenseLocationOrBuilder {
    // Use PoiDenseLocation.newBuilder() to construct.
    private PoiDenseLocation(Builder builder) {
      super(builder);
    }
    private PoiDenseLocation(boolean noInit) {}
    
    private static final PoiDenseLocation defaultInstance;
    public static PoiDenseLocation getDefaultInstance() {
      return defaultInstance;
    }
    
    public PoiDenseLocation getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return PoiDenseLocationPb.internal_static_PoiDenseLocation_descriptor;
    }
    
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return PoiDenseLocationPb.internal_static_PoiDenseLocation_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required int64 terminalId = 1;
    public static final int TERMINALID_FIELD_NUMBER = 1;
    private long terminalId_;
    public boolean hasTerminalId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public long getTerminalId() {
      return terminalId_;
    }
    
    // repeated .RealTimeData dataList = 2;
    public static final int DATALIST_FIELD_NUMBER = 2;
    private java.util.List<RealTimeDataPb.RealTimeData> dataList_;
    public java.util.List<RealTimeDataPb.RealTimeData> getDataListList() {
      return dataList_;
    }
    public java.util.List<? extends RealTimeDataPb.RealTimeDataOrBuilder>
        getDataListOrBuilderList() {
      return dataList_;
    }
    public int getDataListCount() {
      return dataList_.size();
    }
    public RealTimeDataPb.RealTimeData getDataList(int index) {
      return dataList_.get(index);
    }
    public RealTimeDataPb.RealTimeDataOrBuilder getDataListOrBuilder(
        int index) {
      return dataList_.get(index);
    }
    
    private void initFields() {
      terminalId_ = 0L;
      dataList_ = java.util.Collections.emptyList();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasTerminalId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      for (int i = 0; i < getDataListCount(); i++) {
        if (!getDataList(i).isInitialized()) {
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
        output.writeInt64(1, terminalId_);
      }
      for (int i = 0; i < dataList_.size(); i++) {
        output.writeMessage(2, dataList_.get(i));
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
          .computeInt64Size(1, terminalId_);
      }
      for (int i = 0; i < dataList_.size(); i++) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, dataList_.get(i));
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
    
    public static PoiDenseLocation parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static PoiDenseLocation parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static PoiDenseLocation parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static PoiDenseLocation parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static PoiDenseLocation parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static PoiDenseLocation parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static PoiDenseLocation parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static PoiDenseLocation parseDelimitedFrom(
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
    public static PoiDenseLocation parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static PoiDenseLocation parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(PoiDenseLocation prototype) {
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
       implements PoiDenseLocationOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return PoiDenseLocationPb.internal_static_PoiDenseLocation_descriptor;
      }
      
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return PoiDenseLocationPb.internal_static_PoiDenseLocation_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.common.PoiDenseLocationPb.PoiDenseLocation.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getDataListFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        terminalId_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000001);
        if (dataListBuilder_ == null) {
          dataList_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
        } else {
          dataListBuilder_.clear();
        }
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return PoiDenseLocation.getDescriptor();
      }
      
      public PoiDenseLocation getDefaultInstanceForType() {
        return PoiDenseLocation.getDefaultInstance();
      }
      
      public PoiDenseLocation build() {
        PoiDenseLocation result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private PoiDenseLocation buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        PoiDenseLocation result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public PoiDenseLocation buildPartial() {
        PoiDenseLocation result = new PoiDenseLocation(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.terminalId_ = terminalId_;
        if (dataListBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002)) {
            dataList_ = java.util.Collections.unmodifiableList(dataList_);
            bitField0_ = (bitField0_ & ~0x00000002);
          }
          result.dataList_ = dataList_;
        } else {
          result.dataList_ = dataListBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof PoiDenseLocation) {
          return mergeFrom((PoiDenseLocation)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(PoiDenseLocation other) {
        if (other == PoiDenseLocation.getDefaultInstance()) return this;
        if (other.hasTerminalId()) {
          setTerminalId(other.getTerminalId());
        }
        if (dataListBuilder_ == null) {
          if (!other.dataList_.isEmpty()) {
            if (dataList_.isEmpty()) {
              dataList_ = other.dataList_;
              bitField0_ = (bitField0_ & ~0x00000002);
            } else {
              ensureDataListIsMutable();
              dataList_.addAll(other.dataList_);
            }
            onChanged();
          }
        } else {
          if (!other.dataList_.isEmpty()) {
            if (dataListBuilder_.isEmpty()) {
              dataListBuilder_.dispose();
              dataListBuilder_ = null;
              dataList_ = other.dataList_;
              bitField0_ = (bitField0_ & ~0x00000002);
              dataListBuilder_ = 
                com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders ?
                   getDataListFieldBuilder() : null;
            } else {
              dataListBuilder_.addAllMessages(other.dataList_);
            }
          }
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasTerminalId()) {
          
          return false;
        }
        for (int i = 0; i < getDataListCount(); i++) {
          if (!getDataList(i).isInitialized()) {
            
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
              terminalId_ = input.readInt64();
              break;
            }
            case 18: {
              RealTimeDataPb.RealTimeData.Builder subBuilder = RealTimeDataPb.RealTimeData.newBuilder();
              input.readMessage(subBuilder, extensionRegistry);
              addDataList(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required int64 terminalId = 1;
      private long terminalId_ ;
      public boolean hasTerminalId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public long getTerminalId() {
        return terminalId_;
      }
      public Builder setTerminalId(long value) {
        bitField0_ |= 0x00000001;
        terminalId_ = value;
        onChanged();
        return this;
      }
      public Builder clearTerminalId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        terminalId_ = 0L;
        onChanged();
        return this;
      }
      
      // repeated .RealTimeData dataList = 2;
      private java.util.List<RealTimeDataPb.RealTimeData> dataList_ =
        java.util.Collections.emptyList();
      private void ensureDataListIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          dataList_ = new java.util.ArrayList<RealTimeDataPb.RealTimeData>(dataList_);
          bitField0_ |= 0x00000002;
         }
      }
      
      private com.google.protobuf.RepeatedFieldBuilder<
          RealTimeDataPb.RealTimeData, RealTimeDataPb.RealTimeData.Builder, RealTimeDataPb.RealTimeDataOrBuilder> dataListBuilder_;
      
      public java.util.List<RealTimeDataPb.RealTimeData> getDataListList() {
        if (dataListBuilder_ == null) {
          return java.util.Collections.unmodifiableList(dataList_);
        } else {
          return dataListBuilder_.getMessageList();
        }
      }
      public int getDataListCount() {
        if (dataListBuilder_ == null) {
          return dataList_.size();
        } else {
          return dataListBuilder_.getCount();
        }
      }
      public RealTimeDataPb.RealTimeData getDataList(int index) {
        if (dataListBuilder_ == null) {
          return dataList_.get(index);
        } else {
          return dataListBuilder_.getMessage(index);
        }
      }
      public Builder setDataList(
          int index, RealTimeDataPb.RealTimeData value) {
        if (dataListBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureDataListIsMutable();
          dataList_.set(index, value);
          onChanged();
        } else {
          dataListBuilder_.setMessage(index, value);
        }
        return this;
      }
      public Builder setDataList(
          int index, RealTimeDataPb.RealTimeData.Builder builderForValue) {
        if (dataListBuilder_ == null) {
          ensureDataListIsMutable();
          dataList_.set(index, builderForValue.build());
          onChanged();
        } else {
          dataListBuilder_.setMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addDataList(RealTimeDataPb.RealTimeData value) {
        if (dataListBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureDataListIsMutable();
          dataList_.add(value);
          onChanged();
        } else {
          dataListBuilder_.addMessage(value);
        }
        return this;
      }
      public Builder addDataList(
          int index, RealTimeDataPb.RealTimeData value) {
        if (dataListBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          ensureDataListIsMutable();
          dataList_.add(index, value);
          onChanged();
        } else {
          dataListBuilder_.addMessage(index, value);
        }
        return this;
      }
      public Builder addDataList(
          RealTimeDataPb.RealTimeData.Builder builderForValue) {
        if (dataListBuilder_ == null) {
          ensureDataListIsMutable();
          dataList_.add(builderForValue.build());
          onChanged();
        } else {
          dataListBuilder_.addMessage(builderForValue.build());
        }
        return this;
      }
      public Builder addDataList(
          int index, RealTimeDataPb.RealTimeData.Builder builderForValue) {
        if (dataListBuilder_ == null) {
          ensureDataListIsMutable();
          dataList_.add(index, builderForValue.build());
          onChanged();
        } else {
          dataListBuilder_.addMessage(index, builderForValue.build());
        }
        return this;
      }
      public Builder addAllDataList(
          Iterable<? extends RealTimeDataPb.RealTimeData> values) {
        if (dataListBuilder_ == null) {
          ensureDataListIsMutable();
          super.addAll(values, dataList_);
          onChanged();
        } else {
          dataListBuilder_.addAllMessages(values);
        }
        return this;
      }
      public Builder clearDataList() {
        if (dataListBuilder_ == null) {
          dataList_ = java.util.Collections.emptyList();
          bitField0_ = (bitField0_ & ~0x00000002);
          onChanged();
        } else {
          dataListBuilder_.clear();
        }
        return this;
      }
      public Builder removeDataList(int index) {
        if (dataListBuilder_ == null) {
          ensureDataListIsMutable();
          dataList_.remove(index);
          onChanged();
        } else {
          dataListBuilder_.remove(index);
        }
        return this;
      }
      public RealTimeDataPb.RealTimeData.Builder getDataListBuilder(
          int index) {
        return getDataListFieldBuilder().getBuilder(index);
      }
      public RealTimeDataPb.RealTimeDataOrBuilder getDataListOrBuilder(
          int index) {
        if (dataListBuilder_ == null) {
          return dataList_.get(index);  } else {
          return dataListBuilder_.getMessageOrBuilder(index);
        }
      }
      public java.util.List<? extends RealTimeDataPb.RealTimeDataOrBuilder>
           getDataListOrBuilderList() {
        if (dataListBuilder_ != null) {
          return dataListBuilder_.getMessageOrBuilderList();
        } else {
          return java.util.Collections.unmodifiableList(dataList_);
        }
      }
      public RealTimeDataPb.RealTimeData.Builder addDataListBuilder() {
        return getDataListFieldBuilder().addBuilder(
            RealTimeDataPb.RealTimeData.getDefaultInstance());
      }
      public RealTimeDataPb.RealTimeData.Builder addDataListBuilder(
          int index) {
        return getDataListFieldBuilder().addBuilder(
            index, RealTimeDataPb.RealTimeData.getDefaultInstance());
      }
      public java.util.List<RealTimeDataPb.RealTimeData.Builder>
           getDataListBuilderList() {
        return getDataListFieldBuilder().getBuilderList();
      }
      private com.google.protobuf.RepeatedFieldBuilder<
          RealTimeDataPb.RealTimeData, RealTimeDataPb.RealTimeData.Builder, RealTimeDataPb.RealTimeDataOrBuilder>
          getDataListFieldBuilder() {
        if (dataListBuilder_ == null) {
          dataListBuilder_ = new com.google.protobuf.RepeatedFieldBuilder<
              RealTimeDataPb.RealTimeData, RealTimeDataPb.RealTimeData.Builder, RealTimeDataPb.RealTimeDataOrBuilder>(
                  dataList_,
                  ((bitField0_ & 0x00000002) == 0x00000002),
                  getParentForChildren(),
                  isClean());
          dataList_ = null;
        }
        return dataListBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:PoiDenseLocation)
    }
    
    static {
      defaultInstance = new PoiDenseLocation(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:PoiDenseLocation)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_PoiDenseLocation_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_PoiDenseLocation_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n\026PoiDenseLocation.proto\032\022RealTimeData.p" +
      "roto\"G\n\020PoiDenseLocation\022\022\n\nterminalId\030\001" +
      " \002(\003\022\037\n\010dataList\030\002 \003(\0132\r.RealTimeDataBK\n" +
      "5com.navinfo.opentsp.platform.location.p" +
      "rotocol.commonB\022PoiDenseLocationPb"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_PoiDenseLocation_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_PoiDenseLocation_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_PoiDenseLocation_descriptor,
              new String[] { "TerminalId", "DataList", },
              PoiDenseLocation.class,
              PoiDenseLocation.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          RealTimeDataPb.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}