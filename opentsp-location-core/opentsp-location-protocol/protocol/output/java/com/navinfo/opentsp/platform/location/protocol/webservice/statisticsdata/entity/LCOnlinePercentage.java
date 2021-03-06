// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/webservice/statisticsdata/entity/OnlinePercentage.proto

package com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity;

public final class LCOnlinePercentage {
  private LCOnlinePercentage() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface OnlinePercentageOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int64 terminalId = 1;
    boolean hasTerminalId();
    long getTerminalId();
    
    // required int32 statisticDay = 2;
    boolean hasStatisticDay();
    int getStatisticDay();
    
    // required int32 onlineDay = 3;
    boolean hasOnlineDay();
    int getOnlineDay();
    
    // required float onlinePercentage = 4;
    boolean hasOnlinePercentage();
    float getOnlinePercentage();
  }
  public static final class OnlinePercentage extends
      com.google.protobuf.GeneratedMessage
      implements OnlinePercentageOrBuilder {
    // Use OnlinePercentage.newBuilder() to construct.
    private OnlinePercentage(Builder builder) {
      super(builder);
    }
    private OnlinePercentage(boolean noInit) {}
    
    private static final OnlinePercentage defaultInstance;
    public static OnlinePercentage getDefaultInstance() {
      return defaultInstance;
    }
    
    public OnlinePercentage getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.internal_static_OnlinePercentage_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.internal_static_OnlinePercentage_fieldAccessorTable;
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
    
    // required int32 statisticDay = 2;
    public static final int STATISTICDAY_FIELD_NUMBER = 2;
    private int statisticDay_;
    public boolean hasStatisticDay() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public int getStatisticDay() {
      return statisticDay_;
    }
    
    // required int32 onlineDay = 3;
    public static final int ONLINEDAY_FIELD_NUMBER = 3;
    private int onlineDay_;
    public boolean hasOnlineDay() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public int getOnlineDay() {
      return onlineDay_;
    }
    
    // required float onlinePercentage = 4;
    public static final int ONLINEPERCENTAGE_FIELD_NUMBER = 4;
    private float onlinePercentage_;
    public boolean hasOnlinePercentage() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public float getOnlinePercentage() {
      return onlinePercentage_;
    }
    
    private void initFields() {
      terminalId_ = 0L;
      statisticDay_ = 0;
      onlineDay_ = 0;
      onlinePercentage_ = 0F;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasTerminalId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasStatisticDay()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasOnlineDay()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasOnlinePercentage()) {
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
        output.writeInt64(1, terminalId_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, statisticDay_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(3, onlineDay_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeFloat(4, onlinePercentage_);
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
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, statisticDay_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, onlineDay_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeFloatSize(4, onlinePercentage_);
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
    
    public static com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage parseDelimitedFrom(
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
    public static com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage prototype) {
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
       implements com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentageOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.internal_static_OnlinePercentage_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.internal_static_OnlinePercentage_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage.newBuilder()
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
        terminalId_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000001);
        statisticDay_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        onlineDay_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        onlinePercentage_ = 0F;
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage.getDescriptor();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage getDefaultInstanceForType() {
        return com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage.getDefaultInstance();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage build() {
        com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage buildPartial() {
        com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage result = new com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.terminalId_ = terminalId_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.statisticDay_ = statisticDay_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.onlineDay_ = onlineDay_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.onlinePercentage_ = onlinePercentage_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage) {
          return mergeFrom((com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage other) {
        if (other == com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage.getDefaultInstance()) return this;
        if (other.hasTerminalId()) {
          setTerminalId(other.getTerminalId());
        }
        if (other.hasStatisticDay()) {
          setStatisticDay(other.getStatisticDay());
        }
        if (other.hasOnlineDay()) {
          setOnlineDay(other.getOnlineDay());
        }
        if (other.hasOnlinePercentage()) {
          setOnlinePercentage(other.getOnlinePercentage());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasTerminalId()) {
          
          return false;
        }
        if (!hasStatisticDay()) {
          
          return false;
        }
        if (!hasOnlineDay()) {
          
          return false;
        }
        if (!hasOnlinePercentage()) {
          
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
              terminalId_ = input.readInt64();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              statisticDay_ = input.readInt32();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              onlineDay_ = input.readInt32();
              break;
            }
            case 37: {
              bitField0_ |= 0x00000008;
              onlinePercentage_ = input.readFloat();
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
      
      // required int32 statisticDay = 2;
      private int statisticDay_ ;
      public boolean hasStatisticDay() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public int getStatisticDay() {
        return statisticDay_;
      }
      public Builder setStatisticDay(int value) {
        bitField0_ |= 0x00000002;
        statisticDay_ = value;
        onChanged();
        return this;
      }
      public Builder clearStatisticDay() {
        bitField0_ = (bitField0_ & ~0x00000002);
        statisticDay_ = 0;
        onChanged();
        return this;
      }
      
      // required int32 onlineDay = 3;
      private int onlineDay_ ;
      public boolean hasOnlineDay() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public int getOnlineDay() {
        return onlineDay_;
      }
      public Builder setOnlineDay(int value) {
        bitField0_ |= 0x00000004;
        onlineDay_ = value;
        onChanged();
        return this;
      }
      public Builder clearOnlineDay() {
        bitField0_ = (bitField0_ & ~0x00000004);
        onlineDay_ = 0;
        onChanged();
        return this;
      }
      
      // required float onlinePercentage = 4;
      private float onlinePercentage_ ;
      public boolean hasOnlinePercentage() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public float getOnlinePercentage() {
        return onlinePercentage_;
      }
      public Builder setOnlinePercentage(float value) {
        bitField0_ |= 0x00000008;
        onlinePercentage_ = value;
        onChanged();
        return this;
      }
      public Builder clearOnlinePercentage() {
        bitField0_ = (bitField0_ & ~0x00000008);
        onlinePercentage_ = 0F;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:OnlinePercentage)
    }
    
    static {
      defaultInstance = new OnlinePercentage(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:OnlinePercentage)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_OnlinePercentage_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_OnlinePercentage_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\nBcore/proto/webservice/statisticsdata/e" +
      "ntity/OnlinePercentage.proto\"i\n\020OnlinePe" +
      "rcentage\022\022\n\nterminalId\030\001 \002(\003\022\024\n\014statisti" +
      "cDay\030\002 \002(\005\022\021\n\tonlineDay\030\003 \002(\005\022\030\n\020onlineP" +
      "ercentage\030\004 \002(\002Be\nOcom.navinfo.opentsp.p" +
      "latform.location.protocol.webservice.sta" +
      "tisticsdata.entityB\022LCOnlinePercentage"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_OnlinePercentage_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_OnlinePercentage_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_OnlinePercentage_descriptor,
              new java.lang.String[] { "TerminalId", "StatisticDay", "OnlineDay", "OnlinePercentage", },
              com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage.class,
              com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCOnlinePercentage.OnlinePercentage.Builder.class);
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
