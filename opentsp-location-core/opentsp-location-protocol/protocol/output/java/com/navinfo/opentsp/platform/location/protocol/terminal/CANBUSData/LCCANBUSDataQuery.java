// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/terminal/CANBUSData/CANBUSDataQuery.proto

package com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData;

public final class LCCANBUSDataQuery {
  private LCCANBUSDataQuery() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface CANBUSDataQueryOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int32 channelId = 1;
    boolean hasChannelId();
    int getChannelId();
    
    // required int32 interval = 2;
    boolean hasInterval();
    int getInterval();
    
    // optional int32 collectTime = 3;
    boolean hasCollectTime();
    int getCollectTime();
    
    // optional int32 reportInterval = 4;
    boolean hasReportInterval();
    int getReportInterval();
    
    // optional int32 reportType = 5;
    boolean hasReportType();
    int getReportType();
  }
  public static final class CANBUSDataQuery extends
      com.google.protobuf.GeneratedMessage
      implements CANBUSDataQueryOrBuilder {
    // Use CANBUSDataQuery.newBuilder() to construct.
    private CANBUSDataQuery(Builder builder) {
      super(builder);
    }
    private CANBUSDataQuery(boolean noInit) {}
    
    private static final CANBUSDataQuery defaultInstance;
    public static CANBUSDataQuery getDefaultInstance() {
      return defaultInstance;
    }
    
    public CANBUSDataQuery getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.internal_static_CANBUSDataQuery_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.internal_static_CANBUSDataQuery_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required int32 channelId = 1;
    public static final int CHANNELID_FIELD_NUMBER = 1;
    private int channelId_;
    public boolean hasChannelId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public int getChannelId() {
      return channelId_;
    }
    
    // required int32 interval = 2;
    public static final int INTERVAL_FIELD_NUMBER = 2;
    private int interval_;
    public boolean hasInterval() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public int getInterval() {
      return interval_;
    }
    
    // optional int32 collectTime = 3;
    public static final int COLLECTTIME_FIELD_NUMBER = 3;
    private int collectTime_;
    public boolean hasCollectTime() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public int getCollectTime() {
      return collectTime_;
    }
    
    // optional int32 reportInterval = 4;
    public static final int REPORTINTERVAL_FIELD_NUMBER = 4;
    private int reportInterval_;
    public boolean hasReportInterval() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public int getReportInterval() {
      return reportInterval_;
    }
    
    // optional int32 reportType = 5;
    public static final int REPORTTYPE_FIELD_NUMBER = 5;
    private int reportType_;
    public boolean hasReportType() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public int getReportType() {
      return reportType_;
    }
    
    private void initFields() {
      channelId_ = 0;
      interval_ = 0;
      collectTime_ = 0;
      reportInterval_ = 0;
      reportType_ = 0;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasChannelId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasInterval()) {
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
        output.writeInt32(1, channelId_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, interval_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(3, collectTime_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeInt32(4, reportInterval_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeInt32(5, reportType_);
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
          .computeInt32Size(1, channelId_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, interval_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, collectTime_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(4, reportInterval_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(5, reportType_);
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
    
    public static com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery parseDelimitedFrom(
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
    public static com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery prototype) {
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
       implements com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQueryOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.internal_static_CANBUSDataQuery_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.internal_static_CANBUSDataQuery_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery.newBuilder()
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
        channelId_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        interval_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        collectTime_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        reportInterval_ = 0;
        bitField0_ = (bitField0_ & ~0x00000008);
        reportType_ = 0;
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery.getDescriptor();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery getDefaultInstanceForType() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery.getDefaultInstance();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery build() {
        com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery buildPartial() {
        com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery result = new com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.channelId_ = channelId_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.interval_ = interval_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.collectTime_ = collectTime_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.reportInterval_ = reportInterval_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.reportType_ = reportType_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery) {
          return mergeFrom((com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery other) {
        if (other == com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery.getDefaultInstance()) return this;
        if (other.hasChannelId()) {
          setChannelId(other.getChannelId());
        }
        if (other.hasInterval()) {
          setInterval(other.getInterval());
        }
        if (other.hasCollectTime()) {
          setCollectTime(other.getCollectTime());
        }
        if (other.hasReportInterval()) {
          setReportInterval(other.getReportInterval());
        }
        if (other.hasReportType()) {
          setReportType(other.getReportType());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasChannelId()) {
          
          return false;
        }
        if (!hasInterval()) {
          
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
              channelId_ = input.readInt32();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              interval_ = input.readInt32();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              collectTime_ = input.readInt32();
              break;
            }
            case 32: {
              bitField0_ |= 0x00000008;
              reportInterval_ = input.readInt32();
              break;
            }
            case 40: {
              bitField0_ |= 0x00000010;
              reportType_ = input.readInt32();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required int32 channelId = 1;
      private int channelId_ ;
      public boolean hasChannelId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public int getChannelId() {
        return channelId_;
      }
      public Builder setChannelId(int value) {
        bitField0_ |= 0x00000001;
        channelId_ = value;
        onChanged();
        return this;
      }
      public Builder clearChannelId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        channelId_ = 0;
        onChanged();
        return this;
      }
      
      // required int32 interval = 2;
      private int interval_ ;
      public boolean hasInterval() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public int getInterval() {
        return interval_;
      }
      public Builder setInterval(int value) {
        bitField0_ |= 0x00000002;
        interval_ = value;
        onChanged();
        return this;
      }
      public Builder clearInterval() {
        bitField0_ = (bitField0_ & ~0x00000002);
        interval_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 collectTime = 3;
      private int collectTime_ ;
      public boolean hasCollectTime() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public int getCollectTime() {
        return collectTime_;
      }
      public Builder setCollectTime(int value) {
        bitField0_ |= 0x00000004;
        collectTime_ = value;
        onChanged();
        return this;
      }
      public Builder clearCollectTime() {
        bitField0_ = (bitField0_ & ~0x00000004);
        collectTime_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 reportInterval = 4;
      private int reportInterval_ ;
      public boolean hasReportInterval() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public int getReportInterval() {
        return reportInterval_;
      }
      public Builder setReportInterval(int value) {
        bitField0_ |= 0x00000008;
        reportInterval_ = value;
        onChanged();
        return this;
      }
      public Builder clearReportInterval() {
        bitField0_ = (bitField0_ & ~0x00000008);
        reportInterval_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 reportType = 5;
      private int reportType_ ;
      public boolean hasReportType() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public int getReportType() {
        return reportType_;
      }
      public Builder setReportType(int value) {
        bitField0_ |= 0x00000010;
        reportType_ = value;
        onChanged();
        return this;
      }
      public Builder clearReportType() {
        bitField0_ = (bitField0_ & ~0x00000010);
        reportType_ = 0;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:CANBUSDataQuery)
    }
    
    static {
      defaultInstance = new CANBUSDataQuery(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:CANBUSDataQuery)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_CANBUSDataQuery_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_CANBUSDataQuery_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n4core/proto/terminal/CANBUSData/CANBUSD" +
      "ataQuery.proto\"w\n\017CANBUSDataQuery\022\021\n\tcha" +
      "nnelId\030\001 \002(\005\022\020\n\010interval\030\002 \002(\005\022\023\n\013collec" +
      "tTime\030\003 \001(\005\022\026\n\016reportInterval\030\004 \001(\005\022\022\n\nr" +
      "eportType\030\005 \001(\005BW\nBcom.navinfo.opentsp.p" +
      "latform.location.protocol.terminal.CANBU" +
      "SDataB\021LCCANBUSDataQuery"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_CANBUSDataQuery_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_CANBUSDataQuery_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_CANBUSDataQuery_descriptor,
              new java.lang.String[] { "ChannelId", "Interval", "CollectTime", "ReportInterval", "ReportType", },
              com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery.class,
              com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQuery.CANBUSDataQuery.Builder.class);
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
