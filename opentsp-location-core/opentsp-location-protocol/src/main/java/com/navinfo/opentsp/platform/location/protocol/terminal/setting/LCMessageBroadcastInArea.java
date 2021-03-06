// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/terminal/setting/java/MessageBroadcastInArea.proto

package com.navinfo.opentsp.platform.location.protocol.terminal.setting;

public final class LCMessageBroadcastInArea {
  private LCMessageBroadcastInArea() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface MessageBroadcastInAreaOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required .AreaInfo areaInfo = 1;
    boolean hasAreaInfo();
    com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo getAreaInfo();
    com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfoOrBuilder getAreaInfoOrBuilder();
    
    // optional string broadcastContent = 2;
    boolean hasBroadcastContent();
    String getBroadcastContent();
    
    // optional .MessageSign signs = 3;
    boolean hasSigns();
    com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign getSigns();
    com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSignOrBuilder getSignsOrBuilder();
    
    // optional bool saveSign = 4;
    boolean hasSaveSign();
    boolean getSaveSign();
  }
  public static final class MessageBroadcastInArea extends
      com.google.protobuf.GeneratedMessage
      implements MessageBroadcastInAreaOrBuilder {
    // Use MessageBroadcastInArea.newBuilder() to construct.
    private MessageBroadcastInArea(Builder builder) {
      super(builder);
    }
    private MessageBroadcastInArea(boolean noInit) {}
    
    private static final MessageBroadcastInArea defaultInstance;
    public static MessageBroadcastInArea getDefaultInstance() {
      return defaultInstance;
    }
    
    public MessageBroadcastInArea getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return LCMessageBroadcastInArea.internal_static_MessageBroadcastInArea_descriptor;
    }
    
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return LCMessageBroadcastInArea.internal_static_MessageBroadcastInArea_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required .AreaInfo areaInfo = 1;
    public static final int AREAINFO_FIELD_NUMBER = 1;
    private com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo areaInfo_;
    public boolean hasAreaInfo() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo getAreaInfo() {
      return areaInfo_;
    }
    public com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfoOrBuilder getAreaInfoOrBuilder() {
      return areaInfo_;
    }
    
    // optional string broadcastContent = 2;
    public static final int BROADCASTCONTENT_FIELD_NUMBER = 2;
    private Object broadcastContent_;
    public boolean hasBroadcastContent() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getBroadcastContent() {
      Object ref = broadcastContent_;
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
      Object ref = broadcastContent_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        broadcastContent_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // optional .MessageSign signs = 3;
    public static final int SIGNS_FIELD_NUMBER = 3;
    private com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign signs_;
    public boolean hasSigns() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign getSigns() {
      return signs_;
    }
    public com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSignOrBuilder getSignsOrBuilder() {
      return signs_;
    }
    
    // optional bool saveSign = 4;
    public static final int SAVESIGN_FIELD_NUMBER = 4;
    private boolean saveSign_;
    public boolean hasSaveSign() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public boolean getSaveSign() {
      return saveSign_;
    }
    
    private void initFields() {
      areaInfo_ = com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo.getDefaultInstance();
      broadcastContent_ = "";
      signs_ = com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.getDefaultInstance();
      saveSign_ = false;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasAreaInfo()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!getAreaInfo().isInitialized()) {
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
        output.writeMessage(1, areaInfo_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getBroadcastContentBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeMessage(3, signs_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBool(4, saveSign_);
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
          .computeMessageSize(1, areaInfo_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getBroadcastContentBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(3, signs_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(4, saveSign_);
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
    
    public static MessageBroadcastInArea parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static MessageBroadcastInArea parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static MessageBroadcastInArea parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static MessageBroadcastInArea parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static MessageBroadcastInArea parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static MessageBroadcastInArea parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static MessageBroadcastInArea parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static MessageBroadcastInArea parseDelimitedFrom(
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
    public static MessageBroadcastInArea parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static MessageBroadcastInArea parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(MessageBroadcastInArea prototype) {
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
       implements MessageBroadcastInAreaOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return LCMessageBroadcastInArea.internal_static_MessageBroadcastInArea_descriptor;
      }
      
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return LCMessageBroadcastInArea.internal_static_MessageBroadcastInArea_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCMessageBroadcastInArea.MessageBroadcastInArea.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getAreaInfoFieldBuilder();
          getSignsFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        if (areaInfoBuilder_ == null) {
          areaInfo_ = com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo.getDefaultInstance();
        } else {
          areaInfoBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        broadcastContent_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        if (signsBuilder_ == null) {
          signs_ = com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.getDefaultInstance();
        } else {
          signsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        saveSign_ = false;
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return MessageBroadcastInArea.getDescriptor();
      }
      
      public MessageBroadcastInArea getDefaultInstanceForType() {
        return MessageBroadcastInArea.getDefaultInstance();
      }
      
      public MessageBroadcastInArea build() {
        MessageBroadcastInArea result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private MessageBroadcastInArea buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        MessageBroadcastInArea result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public MessageBroadcastInArea buildPartial() {
        MessageBroadcastInArea result = new MessageBroadcastInArea(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        if (areaInfoBuilder_ == null) {
          result.areaInfo_ = areaInfo_;
        } else {
          result.areaInfo_ = areaInfoBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.broadcastContent_ = broadcastContent_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        if (signsBuilder_ == null) {
          result.signs_ = signs_;
        } else {
          result.signs_ = signsBuilder_.build();
        }
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.saveSign_ = saveSign_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof MessageBroadcastInArea) {
          return mergeFrom((MessageBroadcastInArea)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(MessageBroadcastInArea other) {
        if (other == MessageBroadcastInArea.getDefaultInstance()) return this;
        if (other.hasAreaInfo()) {
          mergeAreaInfo(other.getAreaInfo());
        }
        if (other.hasBroadcastContent()) {
          setBroadcastContent(other.getBroadcastContent());
        }
        if (other.hasSigns()) {
          mergeSigns(other.getSigns());
        }
        if (other.hasSaveSign()) {
          setSaveSign(other.getSaveSign());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasAreaInfo()) {
          
          return false;
        }
        if (!getAreaInfo().isInitialized()) {
          
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
            case 10: {
              com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo.Builder subBuilder = com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo.newBuilder();
              if (hasAreaInfo()) {
                subBuilder.mergeFrom(getAreaInfo());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setAreaInfo(subBuilder.buildPartial());
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              broadcastContent_ = input.readBytes();
              break;
            }
            case 26: {
              com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.Builder subBuilder = com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.newBuilder();
              if (hasSigns()) {
                subBuilder.mergeFrom(getSigns());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setSigns(subBuilder.buildPartial());
              break;
            }
            case 32: {
              bitField0_ |= 0x00000008;
              saveSign_ = input.readBool();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required .AreaInfo areaInfo = 1;
      private com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo areaInfo_ = com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo, com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo.Builder, com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfoOrBuilder> areaInfoBuilder_;
      public boolean hasAreaInfo() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo getAreaInfo() {
        if (areaInfoBuilder_ == null) {
          return areaInfo_;
        } else {
          return areaInfoBuilder_.getMessage();
        }
      }
      public Builder setAreaInfo(com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo value) {
        if (areaInfoBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          areaInfo_ = value;
          onChanged();
        } else {
          areaInfoBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      public Builder setAreaInfo(
          com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo.Builder builderForValue) {
        if (areaInfoBuilder_ == null) {
          areaInfo_ = builderForValue.build();
          onChanged();
        } else {
          areaInfoBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      public Builder mergeAreaInfo(com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo value) {
        if (areaInfoBuilder_ == null) {
          if (((bitField0_ & 0x00000001) == 0x00000001) &&
              areaInfo_ != com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo.getDefaultInstance()) {
            areaInfo_ =
              com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo.newBuilder(areaInfo_).mergeFrom(value).buildPartial();
          } else {
            areaInfo_ = value;
          }
          onChanged();
        } else {
          areaInfoBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000001;
        return this;
      }
      public Builder clearAreaInfo() {
        if (areaInfoBuilder_ == null) {
          areaInfo_ = com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo.getDefaultInstance();
          onChanged();
        } else {
          areaInfoBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      public com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo.Builder getAreaInfoBuilder() {
        bitField0_ |= 0x00000001;
        onChanged();
        return getAreaInfoFieldBuilder().getBuilder();
      }
      public com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfoOrBuilder getAreaInfoOrBuilder() {
        if (areaInfoBuilder_ != null) {
          return areaInfoBuilder_.getMessageOrBuilder();
        } else {
          return areaInfo_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo, com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo.Builder, com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfoOrBuilder> 
          getAreaInfoFieldBuilder() {
        if (areaInfoBuilder_ == null) {
          areaInfoBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo, com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo.Builder, com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfoOrBuilder>(
                  areaInfo_,
                  getParentForChildren(),
                  isClean());
          areaInfo_ = null;
        }
        return areaInfoBuilder_;
      }
      
      // optional string broadcastContent = 2;
      private Object broadcastContent_ = "";
      public boolean hasBroadcastContent() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public String getBroadcastContent() {
        Object ref = broadcastContent_;
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
  bitField0_ |= 0x00000002;
        broadcastContent_ = value;
        onChanged();
        return this;
      }
      public Builder clearBroadcastContent() {
        bitField0_ = (bitField0_ & ~0x00000002);
        broadcastContent_ = getDefaultInstance().getBroadcastContent();
        onChanged();
        return this;
      }
      void setBroadcastContent(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        broadcastContent_ = value;
        onChanged();
      }
      
      // optional .MessageSign signs = 3;
      private com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign signs_ = com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign, com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.Builder, com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSignOrBuilder> signsBuilder_;
      public boolean hasSigns() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
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
        bitField0_ |= 0x00000004;
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
        bitField0_ |= 0x00000004;
        return this;
      }
      public Builder mergeSigns(com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign value) {
        if (signsBuilder_ == null) {
          if (((bitField0_ & 0x00000004) == 0x00000004) &&
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
        bitField0_ |= 0x00000004;
        return this;
      }
      public Builder clearSigns() {
        if (signsBuilder_ == null) {
          signs_ = com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.getDefaultInstance();
          onChanged();
        } else {
          signsBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      public com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.Builder getSignsBuilder() {
        bitField0_ |= 0x00000004;
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
      
      // optional bool saveSign = 4;
      private boolean saveSign_ ;
      public boolean hasSaveSign() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public boolean getSaveSign() {
        return saveSign_;
      }
      public Builder setSaveSign(boolean value) {
        bitField0_ |= 0x00000008;
        saveSign_ = value;
        onChanged();
        return this;
      }
      public Builder clearSaveSign() {
        bitField0_ = (bitField0_ & ~0x00000008);
        saveSign_ = false;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:MessageBroadcastInArea)
    }
    
    static {
      defaultInstance = new MessageBroadcastInArea(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:MessageBroadcastInArea)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_MessageBroadcastInArea_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MessageBroadcastInArea_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n=core/proto/terminal/setting/java/Messa" +
      "geBroadcastInArea.proto\0320core/proto/data" +
      "access/common/java/AreaInfo.proto\032(core/" +
      "proto/common/java/MessageSign.proto\"~\n\026M" +
      "essageBroadcastInArea\022\033\n\010areaInfo\030\001 \002(\0132" +
      "\t.AreaInfo\022\030\n\020broadcastContent\030\002 \001(\t\022\033\n\005" +
      "signs\030\003 \001(\0132\014.MessageSign\022\020\n\010saveSign\030\004 " +
      "\001(\010B[\n?com.navinfo.opentsp.platform.loca" +
      "tion.protocol.terminal.settingB\030LCMessag" +
      "eBroadcastInArea"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_MessageBroadcastInArea_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_MessageBroadcastInArea_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_MessageBroadcastInArea_descriptor,
              new String[] { "AreaInfo", "BroadcastContent", "Signs", "SaveSign", },
              MessageBroadcastInArea.class,
              MessageBroadcastInArea.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.getDescriptor(),
          com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
