// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/common/java/MessageSign.proto

package com.navinfo.opentsp.platform.location.protocol.common;

public final class LCMessageSign {
  private LCMessageSign() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface MessageSignOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required bool isUrgent = 1;
    boolean hasIsUrgent();
    boolean getIsUrgent();
    
    // required bool isDisplay = 2;
    boolean hasIsDisplay();
    boolean getIsDisplay();
    
    // required bool isBroadcast = 3;
    boolean hasIsBroadcast();
    boolean getIsBroadcast();
    
    // required bool isAdvertiseScreen = 4;
    boolean hasIsAdvertiseScreen();
    boolean getIsAdvertiseScreen();
    
    // required bool infoType = 5;
    boolean hasInfoType();
    boolean getInfoType();
  }
  public static final class MessageSign extends
      com.google.protobuf.GeneratedMessage
      implements MessageSignOrBuilder {
    // Use MessageSign.newBuilder() to construct.
    private MessageSign(Builder builder) {
      super(builder);
    }
    private MessageSign(boolean noInit) {}
    
    private static final MessageSign defaultInstance;
    public static MessageSign getDefaultInstance() {
      return defaultInstance;
    }
    
    public MessageSign getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.internal_static_MessageSign_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.internal_static_MessageSign_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required bool isUrgent = 1;
    public static final int ISURGENT_FIELD_NUMBER = 1;
    private boolean isUrgent_;
    public boolean hasIsUrgent() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public boolean getIsUrgent() {
      return isUrgent_;
    }
    
    // required bool isDisplay = 2;
    public static final int ISDISPLAY_FIELD_NUMBER = 2;
    private boolean isDisplay_;
    public boolean hasIsDisplay() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public boolean getIsDisplay() {
      return isDisplay_;
    }
    
    // required bool isBroadcast = 3;
    public static final int ISBROADCAST_FIELD_NUMBER = 3;
    private boolean isBroadcast_;
    public boolean hasIsBroadcast() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public boolean getIsBroadcast() {
      return isBroadcast_;
    }
    
    // required bool isAdvertiseScreen = 4;
    public static final int ISADVERTISESCREEN_FIELD_NUMBER = 4;
    private boolean isAdvertiseScreen_;
    public boolean hasIsAdvertiseScreen() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public boolean getIsAdvertiseScreen() {
      return isAdvertiseScreen_;
    }
    
    // required bool infoType = 5;
    public static final int INFOTYPE_FIELD_NUMBER = 5;
    private boolean infoType_;
    public boolean hasInfoType() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public boolean getInfoType() {
      return infoType_;
    }
    
    private void initFields() {
      isUrgent_ = false;
      isDisplay_ = false;
      isBroadcast_ = false;
      isAdvertiseScreen_ = false;
      infoType_ = false;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasIsUrgent()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasIsDisplay()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasIsBroadcast()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasIsAdvertiseScreen()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasInfoType()) {
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
        output.writeBool(1, isUrgent_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBool(2, isDisplay_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBool(3, isBroadcast_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeBool(4, isAdvertiseScreen_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeBool(5, infoType_);
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
          .computeBoolSize(1, isUrgent_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(2, isDisplay_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(3, isBroadcast_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(4, isAdvertiseScreen_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(5, infoType_);
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
    
    public static com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign parseDelimitedFrom(
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
    public static com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign prototype) {
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
       implements com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSignOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.internal_static_MessageSign_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.internal_static_MessageSign_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.newBuilder()
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
        isUrgent_ = false;
        bitField0_ = (bitField0_ & ~0x00000001);
        isDisplay_ = false;
        bitField0_ = (bitField0_ & ~0x00000002);
        isBroadcast_ = false;
        bitField0_ = (bitField0_ & ~0x00000004);
        isAdvertiseScreen_ = false;
        bitField0_ = (bitField0_ & ~0x00000008);
        infoType_ = false;
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.getDescriptor();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign getDefaultInstanceForType() {
        return com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.getDefaultInstance();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign build() {
        com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign buildPartial() {
        com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign result = new com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.isUrgent_ = isUrgent_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.isDisplay_ = isDisplay_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.isBroadcast_ = isBroadcast_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.isAdvertiseScreen_ = isAdvertiseScreen_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.infoType_ = infoType_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign) {
          return mergeFrom((com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign other) {
        if (other == com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.getDefaultInstance()) return this;
        if (other.hasIsUrgent()) {
          setIsUrgent(other.getIsUrgent());
        }
        if (other.hasIsDisplay()) {
          setIsDisplay(other.getIsDisplay());
        }
        if (other.hasIsBroadcast()) {
          setIsBroadcast(other.getIsBroadcast());
        }
        if (other.hasIsAdvertiseScreen()) {
          setIsAdvertiseScreen(other.getIsAdvertiseScreen());
        }
        if (other.hasInfoType()) {
          setInfoType(other.getInfoType());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasIsUrgent()) {
          
          return false;
        }
        if (!hasIsDisplay()) {
          
          return false;
        }
        if (!hasIsBroadcast()) {
          
          return false;
        }
        if (!hasIsAdvertiseScreen()) {
          
          return false;
        }
        if (!hasInfoType()) {
          
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
              isUrgent_ = input.readBool();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              isDisplay_ = input.readBool();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              isBroadcast_ = input.readBool();
              break;
            }
            case 32: {
              bitField0_ |= 0x00000008;
              isAdvertiseScreen_ = input.readBool();
              break;
            }
            case 40: {
              bitField0_ |= 0x00000010;
              infoType_ = input.readBool();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required bool isUrgent = 1;
      private boolean isUrgent_ ;
      public boolean hasIsUrgent() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public boolean getIsUrgent() {
        return isUrgent_;
      }
      public Builder setIsUrgent(boolean value) {
        bitField0_ |= 0x00000001;
        isUrgent_ = value;
        onChanged();
        return this;
      }
      public Builder clearIsUrgent() {
        bitField0_ = (bitField0_ & ~0x00000001);
        isUrgent_ = false;
        onChanged();
        return this;
      }
      
      // required bool isDisplay = 2;
      private boolean isDisplay_ ;
      public boolean hasIsDisplay() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public boolean getIsDisplay() {
        return isDisplay_;
      }
      public Builder setIsDisplay(boolean value) {
        bitField0_ |= 0x00000002;
        isDisplay_ = value;
        onChanged();
        return this;
      }
      public Builder clearIsDisplay() {
        bitField0_ = (bitField0_ & ~0x00000002);
        isDisplay_ = false;
        onChanged();
        return this;
      }
      
      // required bool isBroadcast = 3;
      private boolean isBroadcast_ ;
      public boolean hasIsBroadcast() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public boolean getIsBroadcast() {
        return isBroadcast_;
      }
      public Builder setIsBroadcast(boolean value) {
        bitField0_ |= 0x00000004;
        isBroadcast_ = value;
        onChanged();
        return this;
      }
      public Builder clearIsBroadcast() {
        bitField0_ = (bitField0_ & ~0x00000004);
        isBroadcast_ = false;
        onChanged();
        return this;
      }
      
      // required bool isAdvertiseScreen = 4;
      private boolean isAdvertiseScreen_ ;
      public boolean hasIsAdvertiseScreen() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public boolean getIsAdvertiseScreen() {
        return isAdvertiseScreen_;
      }
      public Builder setIsAdvertiseScreen(boolean value) {
        bitField0_ |= 0x00000008;
        isAdvertiseScreen_ = value;
        onChanged();
        return this;
      }
      public Builder clearIsAdvertiseScreen() {
        bitField0_ = (bitField0_ & ~0x00000008);
        isAdvertiseScreen_ = false;
        onChanged();
        return this;
      }
      
      // required bool infoType = 5;
      private boolean infoType_ ;
      public boolean hasInfoType() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public boolean getInfoType() {
        return infoType_;
      }
      public Builder setInfoType(boolean value) {
        bitField0_ |= 0x00000010;
        infoType_ = value;
        onChanged();
        return this;
      }
      public Builder clearInfoType() {
        bitField0_ = (bitField0_ & ~0x00000010);
        infoType_ = false;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:MessageSign)
    }
    
    static {
      defaultInstance = new MessageSign(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:MessageSign)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_MessageSign_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MessageSign_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n(core/proto/common/java/MessageSign.pro" +
      "to\"t\n\013MessageSign\022\020\n\010isUrgent\030\001 \002(\010\022\021\n\ti" +
      "sDisplay\030\002 \002(\010\022\023\n\013isBroadcast\030\003 \002(\010\022\031\n\021i" +
      "sAdvertiseScreen\030\004 \002(\010\022\020\n\010infoType\030\005 \002(\010" +
      "BF\n5com.navinfo.opentsp.platform.locatio" +
      "n.protocol.commonB\rLCMessageSign"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_MessageSign_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_MessageSign_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_MessageSign_descriptor,
              new java.lang.String[] { "IsUrgent", "IsDisplay", "IsBroadcast", "IsAdvertiseScreen", "InfoType", },
              com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.class,
              com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign.MessageSign.Builder.class);
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