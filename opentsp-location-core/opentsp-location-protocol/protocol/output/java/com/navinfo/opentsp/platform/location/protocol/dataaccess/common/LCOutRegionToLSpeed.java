// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/dataaccess/common/java/OutRegionToLSpeed.proto

package com.navinfo.opentsp.platform.location.protocol.dataaccess.common;

public final class LCOutRegionToLSpeed {
  private LCOutRegionToLSpeed() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface OutRegionToLSpeedOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int64 areaId = 1;
    boolean hasAreaId();
    long getAreaId();
    
    // required int32 controlType = 2;
    boolean hasControlType();
    int getControlType();
    
    // required int32 limitSpeed = 3;
    boolean hasLimitSpeed();
    int getLimitSpeed();
    
    // required string gpsId = 4;
    boolean hasGpsId();
    String getGpsId();
    
    // optional string broadcastContent = 5;
    boolean hasBroadcastContent();
    String getBroadcastContent();
    
    // optional .MessageSign signs = 6;
    boolean hasSigns();
    com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign getSigns();
    com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSignOrBuilder getSignsOrBuilder();
  }
  public static final class OutRegionToLSpeed extends
      com.google.protobuf.GeneratedMessage
      implements OutRegionToLSpeedOrBuilder {
    // Use OutRegionToLSpeed.newBuilder() to construct.
    private OutRegionToLSpeed(Builder builder) {
      super(builder);
    }
    private OutRegionToLSpeed(boolean noInit) {}
    
    private static final OutRegionToLSpeed defaultInstance;
    public static OutRegionToLSpeed getDefaultInstance() {
      return defaultInstance;
    }
    
    public OutRegionToLSpeed getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.internal_static_OutRegionToLSpeed_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.internal_static_OutRegionToLSpeed_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required int64 areaId = 1;
    public static final int AREAID_FIELD_NUMBER = 1;
    private long areaId_;
    public boolean hasAreaId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public long getAreaId() {
      return areaId_;
    }
    
    // required int32 controlType = 2;
    public static final int CONTROLTYPE_FIELD_NUMBER = 2;
    private int controlType_;
    public boolean hasControlType() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public int getControlType() {
      return controlType_;
    }
    
    // required int32 limitSpeed = 3;
    public static final int LIMITSPEED_FIELD_NUMBER = 3;
    private int limitSpeed_;
    public boolean hasLimitSpeed() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public int getLimitSpeed() {
      return limitSpeed_;
    }
    
    // required string gpsId = 4;
    public static final int GPSID_FIELD_NUMBER = 4;
    private java.lang.Object gpsId_;
    public boolean hasGpsId() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public String getGpsId() {
      java.lang.Object ref = gpsId_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          gpsId_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getGpsIdBytes() {
      java.lang.Object ref = gpsId_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        gpsId_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional string broadcastContent = 5;
    public static final int BROADCASTCONTENT_FIELD_NUMBER = 5;
    private java.lang.Object broadcastContent_;
    public boolean hasBroadcastContent() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public String getBroadcastContent() {
      java.lang.Object ref = broadcastContent_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          broadcastContent_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getBroadcastContentBytes() {
      java.lang.Object ref = broadcastContent_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        broadcastContent_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional .MessageSign signs = 6;
    public static final int SIGNS_FIELD_NUMBER = 6;
    private com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign signs_;
    public boolean hasSigns() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    public com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign getSigns() {
      return signs_;
    }
    public com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSignOrBuilder getSignsOrBuilder() {
      return signs_;
    }
    
    private void initFields() {
      areaId_ = 0L;
      controlType_ = 0;
      limitSpeed_ = 0;
      gpsId_ = "";
      broadcastContent_ = "";
      signs_ = com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasAreaId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasControlType()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasLimitSpeed()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasGpsId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (hasSigns()) {
        if (!getSigns().isInitialized()) {
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
        output.writeInt64(1, areaId_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, controlType_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(3, limitSpeed_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBytes(4, getGpsIdBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBytes(5, getBroadcastContentBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeMessage(6, signs_);
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
          .computeInt64Size(1, areaId_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, controlType_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, limitSpeed_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(4, getGpsIdBytes());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(5, getBroadcastContentBytes());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(6, signs_);
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
    
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed parseDelimitedFrom(
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
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed prototype) {
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
       implements com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeedOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.internal_static_OutRegionToLSpeed_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.internal_static_OutRegionToLSpeed_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getSignsFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        areaId_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000001);
        controlType_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        limitSpeed_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        gpsId_ = "";
        bitField0_ = (bitField0_ & ~0x00000008);
        broadcastContent_ = "";
        bitField0_ = (bitField0_ & ~0x00000010);
        if (signsBuilder_ == null) {
          signs_ = com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.getDefaultInstance();
        } else {
          signsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000020);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed.getDescriptor();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed getDefaultInstanceForType() {
        return com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed.getDefaultInstance();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed build() {
        com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed buildPartial() {
        com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed result = new com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.areaId_ = areaId_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.controlType_ = controlType_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.limitSpeed_ = limitSpeed_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.gpsId_ = gpsId_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.broadcastContent_ = broadcastContent_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        if (signsBuilder_ == null) {
          result.signs_ = signs_;
        } else {
          result.signs_ = signsBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed) {
          return mergeFrom((com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed other) {
        if (other == com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed.getDefaultInstance()) return this;
        if (other.hasAreaId()) {
          setAreaId(other.getAreaId());
        }
        if (other.hasControlType()) {
          setControlType(other.getControlType());
        }
        if (other.hasLimitSpeed()) {
          setLimitSpeed(other.getLimitSpeed());
        }
        if (other.hasGpsId()) {
          setGpsId(other.getGpsId());
        }
        if (other.hasBroadcastContent()) {
          setBroadcastContent(other.getBroadcastContent());
        }
        if (other.hasSigns()) {
          mergeSigns(other.getSigns());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasAreaId()) {
          
          return false;
        }
        if (!hasControlType()) {
          
          return false;
        }
        if (!hasLimitSpeed()) {
          
          return false;
        }
        if (!hasGpsId()) {
          
          return false;
        }
        if (hasSigns()) {
          if (!getSigns().isInitialized()) {
            
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
              areaId_ = input.readInt64();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              controlType_ = input.readInt32();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              limitSpeed_ = input.readInt32();
              break;
            }
            case 34: {
              bitField0_ |= 0x00000008;
              gpsId_ = input.readBytes();
              break;
            }
            case 42: {
              bitField0_ |= 0x00000010;
              broadcastContent_ = input.readBytes();
              break;
            }
            case 50: {
              com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.Builder subBuilder = com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.newBuilder();
              if (hasSigns()) {
                subBuilder.mergeFrom(getSigns());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setSigns(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required int64 areaId = 1;
      private long areaId_ ;
      public boolean hasAreaId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public long getAreaId() {
        return areaId_;
      }
      public Builder setAreaId(long value) {
        bitField0_ |= 0x00000001;
        areaId_ = value;
        onChanged();
        return this;
      }
      public Builder clearAreaId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        areaId_ = 0L;
        onChanged();
        return this;
      }
      
      // required int32 controlType = 2;
      private int controlType_ ;
      public boolean hasControlType() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public int getControlType() {
        return controlType_;
      }
      public Builder setControlType(int value) {
        bitField0_ |= 0x00000002;
        controlType_ = value;
        onChanged();
        return this;
      }
      public Builder clearControlType() {
        bitField0_ = (bitField0_ & ~0x00000002);
        controlType_ = 0;
        onChanged();
        return this;
      }
      
      // required int32 limitSpeed = 3;
      private int limitSpeed_ ;
      public boolean hasLimitSpeed() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public int getLimitSpeed() {
        return limitSpeed_;
      }
      public Builder setLimitSpeed(int value) {
        bitField0_ |= 0x00000004;
        limitSpeed_ = value;
        onChanged();
        return this;
      }
      public Builder clearLimitSpeed() {
        bitField0_ = (bitField0_ & ~0x00000004);
        limitSpeed_ = 0;
        onChanged();
        return this;
      }
      
      // required string gpsId = 4;
      private java.lang.Object gpsId_ = "";
      public boolean hasGpsId() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public String getGpsId() {
        java.lang.Object ref = gpsId_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          gpsId_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setGpsId(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000008;
        gpsId_ = value;
        onChanged();
        return this;
      }
      public Builder clearGpsId() {
        bitField0_ = (bitField0_ & ~0x00000008);
        gpsId_ = getDefaultInstance().getGpsId();
        onChanged();
        return this;
      }
      void setGpsId(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000008;
        gpsId_ = value;
        onChanged();
      }
      
      // optional string broadcastContent = 5;
      private java.lang.Object broadcastContent_ = "";
      public boolean hasBroadcastContent() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public String getBroadcastContent() {
        java.lang.Object ref = broadcastContent_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          broadcastContent_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setBroadcastContent(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000010;
        broadcastContent_ = value;
        onChanged();
        return this;
      }
      public Builder clearBroadcastContent() {
        bitField0_ = (bitField0_ & ~0x00000010);
        broadcastContent_ = getDefaultInstance().getBroadcastContent();
        onChanged();
        return this;
      }
      void setBroadcastContent(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000010;
        broadcastContent_ = value;
        onChanged();
      }
      
      // optional .MessageSign signs = 6;
      private com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign signs_ = com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign, com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.Builder, com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSignOrBuilder> signsBuilder_;
      public boolean hasSigns() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      public com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign getSigns() {
        if (signsBuilder_ == null) {
          return signs_;
        } else {
          return signsBuilder_.getMessage();
        }
      }
      public Builder setSigns(com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign value) {
        if (signsBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          signs_ = value;
          onChanged();
        } else {
          signsBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000020;
        return this;
      }
      public Builder setSigns(
          com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.Builder builderForValue) {
        if (signsBuilder_ == null) {
          signs_ = builderForValue.build();
          onChanged();
        } else {
          signsBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000020;
        return this;
      }
      public Builder mergeSigns(com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign value) {
        if (signsBuilder_ == null) {
          if (((bitField0_ & 0x00000020) == 0x00000020) &&
              signs_ != com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.getDefaultInstance()) {
            signs_ =
              com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.newBuilder(signs_).mergeFrom(value).buildPartial();
          } else {
            signs_ = value;
          }
          onChanged();
        } else {
          signsBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000020;
        return this;
      }
      public Builder clearSigns() {
        if (signsBuilder_ == null) {
          signs_ = com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.getDefaultInstance();
          onChanged();
        } else {
          signsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000020);
        return this;
      }
      public com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.Builder getSignsBuilder() {
        bitField0_ |= 0x00000020;
        onChanged();
        return getSignsFieldBuilder().getBuilder();
      }
      public com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSignOrBuilder getSignsOrBuilder() {
        if (signsBuilder_ != null) {
          return signsBuilder_.getMessageOrBuilder();
        } else {
          return signs_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign, com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.Builder, com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSignOrBuilder> 
          getSignsFieldBuilder() {
        if (signsBuilder_ == null) {
          signsBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign, com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.Builder, com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSignOrBuilder>(
                  signs_,
                  getParentForChildren(),
                  isClean());
          signs_ = null;
        }
        return signsBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:OutRegionToLSpeed)
    }
    
    static {
      defaultInstance = new OutRegionToLSpeed(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:OutRegionToLSpeed)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_OutRegionToLSpeed_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_OutRegionToLSpeed_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n9core/proto/dataaccess/common/java/OutR" +
      "egionToLSpeed.proto\032(core/proto/common/j" +
      "ava/MessageSign.proto\"\222\001\n\021OutRegionToLSp" +
      "eed\022\016\n\006areaId\030\001 \002(\003\022\023\n\013controlType\030\002 \002(\005" +
      "\022\022\n\nlimitSpeed\030\003 \002(\005\022\r\n\005gpsId\030\004 \002(\t\022\030\n\020b" +
      "roadcastContent\030\005 \001(\t\022\033\n\005signs\030\006 \001(\0132\014.M" +
      "essageSignBW\n@com.navinfo.opentsp.platfo" +
      "rm.location.protocol.dataaccess.commonB\023" +
      "LCOutRegionToLSpeed"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_OutRegionToLSpeed_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_OutRegionToLSpeed_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_OutRegionToLSpeed_descriptor,
              new java.lang.String[] { "AreaId", "ControlType", "LimitSpeed", "GpsId", "BroadcastContent", "Signs", },
              com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed.class,
              com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCOutRegionToLSpeed.OutRegionToLSpeed.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
