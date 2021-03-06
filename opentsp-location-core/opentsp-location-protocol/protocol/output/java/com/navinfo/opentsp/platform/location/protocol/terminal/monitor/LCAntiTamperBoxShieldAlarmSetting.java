// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/terminal/monitor/java/AntiTamperBoxShieldAlarmSetting.proto

package com.navinfo.opentsp.platform.location.protocol.terminal.monitor;

public final class LCAntiTamperBoxShieldAlarmSetting {
  private LCAntiTamperBoxShieldAlarmSetting() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface AntiTamperBoxShieldAlarmSettingOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // optional bytes shieldingSetting = 1;
    boolean hasShieldingSetting();
    com.google.protobuf.ByteString getShieldingSetting();
  }
  public static final class AntiTamperBoxShieldAlarmSetting extends
      com.google.protobuf.GeneratedMessage
      implements AntiTamperBoxShieldAlarmSettingOrBuilder {
    // Use AntiTamperBoxShieldAlarmSetting.newBuilder() to construct.
    private AntiTamperBoxShieldAlarmSetting(Builder builder) {
      super(builder);
    }
    private AntiTamperBoxShieldAlarmSetting(boolean noInit) {}
    
    private static final AntiTamperBoxShieldAlarmSetting defaultInstance;
    public static AntiTamperBoxShieldAlarmSetting getDefaultInstance() {
      return defaultInstance;
    }
    
    public AntiTamperBoxShieldAlarmSetting getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.internal_static_AntiTamperBoxShieldAlarmSetting_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.internal_static_AntiTamperBoxShieldAlarmSetting_fieldAccessorTable;
    }
    
    private int bitField0_;
    // optional bytes shieldingSetting = 1;
    public static final int SHIELDINGSETTING_FIELD_NUMBER = 1;
    private com.google.protobuf.ByteString shieldingSetting_;
    public boolean hasShieldingSetting() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public com.google.protobuf.ByteString getShieldingSetting() {
      return shieldingSetting_;
    }
    
    private void initFields() {
      shieldingSetting_ = com.google.protobuf.ByteString.EMPTY;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeBytes(1, shieldingSetting_);
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
          .computeBytesSize(1, shieldingSetting_);
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
    
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting parseDelimitedFrom(
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
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting prototype) {
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
       implements com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSettingOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.internal_static_AntiTamperBoxShieldAlarmSetting_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.internal_static_AntiTamperBoxShieldAlarmSetting_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting.newBuilder()
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
        shieldingSetting_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting.getDescriptor();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting getDefaultInstanceForType() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting.getDefaultInstance();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting build() {
        com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting buildPartial() {
        com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting result = new com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.shieldingSetting_ = shieldingSetting_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting) {
          return mergeFrom((com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting other) {
        if (other == com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting.getDefaultInstance()) return this;
        if (other.hasShieldingSetting()) {
          setShieldingSetting(other.getShieldingSetting());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
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
              bitField0_ |= 0x00000001;
              shieldingSetting_ = input.readBytes();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // optional bytes shieldingSetting = 1;
      private com.google.protobuf.ByteString shieldingSetting_ = com.google.protobuf.ByteString.EMPTY;
      public boolean hasShieldingSetting() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public com.google.protobuf.ByteString getShieldingSetting() {
        return shieldingSetting_;
      }
      public Builder setShieldingSetting(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000001;
        shieldingSetting_ = value;
        onChanged();
        return this;
      }
      public Builder clearShieldingSetting() {
        bitField0_ = (bitField0_ & ~0x00000001);
        shieldingSetting_ = getDefaultInstance().getShieldingSetting();
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:AntiTamperBoxShieldAlarmSetting)
    }
    
    static {
      defaultInstance = new AntiTamperBoxShieldAlarmSetting(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:AntiTamperBoxShieldAlarmSetting)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_AntiTamperBoxShieldAlarmSetting_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_AntiTamperBoxShieldAlarmSetting_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\nFcore/proto/terminal/monitor/java/AntiT" +
      "amperBoxShieldAlarmSetting.proto\";\n\037Anti" +
      "TamperBoxShieldAlarmSetting\022\030\n\020shielding" +
      "Setting\030\001 \001(\014Bd\n?com.navinfo.opentsp.pla" +
      "tform.location.protocol.terminal.monitor" +
      "B!LCAntiTamperBoxShieldAlarmSetting"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_AntiTamperBoxShieldAlarmSetting_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_AntiTamperBoxShieldAlarmSetting_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_AntiTamperBoxShieldAlarmSetting_descriptor,
              new java.lang.String[] { "ShieldingSetting", },
              com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting.class,
              com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAntiTamperBoxShieldAlarmSetting.AntiTamperBoxShieldAlarmSetting.Builder.class);
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
