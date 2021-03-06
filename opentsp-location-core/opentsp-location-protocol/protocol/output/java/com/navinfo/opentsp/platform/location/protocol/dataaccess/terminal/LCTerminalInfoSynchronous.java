// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/dataaccess/terminal/java/TerminalInfoSynchronous.proto

package com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal;

public final class LCTerminalInfoSynchronous {
  private LCTerminalInfoSynchronous() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface TerminalInfoSynchronousOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required .OperationType type = 1;
    boolean hasType();
    com.navinfo.opentsp.platform.location.protocol.common.LCOperationType.OperationType getType();
    
    // required .TerminalInfo info = 2;
    boolean hasInfo();
    com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo getInfo();
    com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfoOrBuilder getInfoOrBuilder();
  }
  public static final class TerminalInfoSynchronous extends
      com.google.protobuf.GeneratedMessage
      implements TerminalInfoSynchronousOrBuilder {
    // Use TerminalInfoSynchronous.newBuilder() to construct.
    private TerminalInfoSynchronous(Builder builder) {
      super(builder);
    }
    private TerminalInfoSynchronous(boolean noInit) {}
    
    private static final TerminalInfoSynchronous defaultInstance;
    public static TerminalInfoSynchronous getDefaultInstance() {
      return defaultInstance;
    }
    
    public TerminalInfoSynchronous getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.internal_static_TerminalInfoSynchronous_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.internal_static_TerminalInfoSynchronous_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required .OperationType type = 1;
    public static final int TYPE_FIELD_NUMBER = 1;
    private com.navinfo.opentsp.platform.location.protocol.common.LCOperationType.OperationType type_;
    public boolean hasType() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public com.navinfo.opentsp.platform.location.protocol.common.LCOperationType.OperationType getType() {
      return type_;
    }
    
    // required .TerminalInfo info = 2;
    public static final int INFO_FIELD_NUMBER = 2;
    private com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo info_;
    public boolean hasInfo() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo getInfo() {
      return info_;
    }
    public com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfoOrBuilder getInfoOrBuilder() {
      return info_;
    }
    
    private void initFields() {
      type_ = com.navinfo.opentsp.platform.location.protocol.common.LCOperationType.OperationType.add;
      info_ = com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasType()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasInfo()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!getInfo().isInitialized()) {
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
        output.writeEnum(1, type_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeMessage(2, info_);
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
          .computeEnumSize(1, type_.getNumber());
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(2, info_);
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
    
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous parseDelimitedFrom(
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
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous prototype) {
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
       implements com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronousOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.internal_static_TerminalInfoSynchronous_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.internal_static_TerminalInfoSynchronous_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getInfoFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        type_ = com.navinfo.opentsp.platform.location.protocol.common.LCOperationType.OperationType.add;
        bitField0_ = (bitField0_ & ~0x00000001);
        if (infoBuilder_ == null) {
          info_ = com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo.getDefaultInstance();
        } else {
          infoBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous.getDescriptor();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous getDefaultInstanceForType() {
        return com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous.getDefaultInstance();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous build() {
        com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous buildPartial() {
        com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous result = new com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.type_ = type_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        if (infoBuilder_ == null) {
          result.info_ = info_;
        } else {
          result.info_ = infoBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous) {
          return mergeFrom((com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous other) {
        if (other == com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous.getDefaultInstance()) return this;
        if (other.hasType()) {
          setType(other.getType());
        }
        if (other.hasInfo()) {
          mergeInfo(other.getInfo());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasType()) {
          
          return false;
        }
        if (!hasInfo()) {
          
          return false;
        }
        if (!getInfo().isInitialized()) {
          
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
              int rawValue = input.readEnum();
              com.navinfo.opentsp.platform.location.protocol.common.LCOperationType.OperationType value = com.navinfo.opentsp.platform.location.protocol.common.LCOperationType.OperationType.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(1, rawValue);
              } else {
                bitField0_ |= 0x00000001;
                type_ = value;
              }
              break;
            }
            case 18: {
              com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo.Builder subBuilder = com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo.newBuilder();
              if (hasInfo()) {
                subBuilder.mergeFrom(getInfo());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setInfo(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required .OperationType type = 1;
      private com.navinfo.opentsp.platform.location.protocol.common.LCOperationType.OperationType type_ = com.navinfo.opentsp.platform.location.protocol.common.LCOperationType.OperationType.add;
      public boolean hasType() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public com.navinfo.opentsp.platform.location.protocol.common.LCOperationType.OperationType getType() {
        return type_;
      }
      public Builder setType(com.navinfo.opentsp.platform.location.protocol.common.LCOperationType.OperationType value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000001;
        type_ = value;
        onChanged();
        return this;
      }
      public Builder clearType() {
        bitField0_ = (bitField0_ & ~0x00000001);
        type_ = com.navinfo.opentsp.platform.location.protocol.common.LCOperationType.OperationType.add;
        onChanged();
        return this;
      }
      
      // required .TerminalInfo info = 2;
      private com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo info_ = com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo, com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo.Builder, com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfoOrBuilder> infoBuilder_;
      public boolean hasInfo() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo getInfo() {
        if (infoBuilder_ == null) {
          return info_;
        } else {
          return infoBuilder_.getMessage();
        }
      }
      public Builder setInfo(com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo value) {
        if (infoBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          info_ = value;
          onChanged();
        } else {
          infoBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      public Builder setInfo(
          com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo.Builder builderForValue) {
        if (infoBuilder_ == null) {
          info_ = builderForValue.build();
          onChanged();
        } else {
          infoBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      public Builder mergeInfo(com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo value) {
        if (infoBuilder_ == null) {
          if (((bitField0_ & 0x00000002) == 0x00000002) &&
              info_ != com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo.getDefaultInstance()) {
            info_ =
              com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo.newBuilder(info_).mergeFrom(value).buildPartial();
          } else {
            info_ = value;
          }
          onChanged();
        } else {
          infoBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000002;
        return this;
      }
      public Builder clearInfo() {
        if (infoBuilder_ == null) {
          info_ = com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo.getDefaultInstance();
          onChanged();
        } else {
          infoBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      public com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo.Builder getInfoBuilder() {
        bitField0_ |= 0x00000002;
        onChanged();
        return getInfoFieldBuilder().getBuilder();
      }
      public com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfoOrBuilder getInfoOrBuilder() {
        if (infoBuilder_ != null) {
          return infoBuilder_.getMessageOrBuilder();
        } else {
          return info_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo, com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo.Builder, com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfoOrBuilder> 
          getInfoFieldBuilder() {
        if (infoBuilder_ == null) {
          infoBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo, com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo.Builder, com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfoOrBuilder>(
                  info_,
                  getParentForChildren(),
                  isClean());
          info_ = null;
        }
        return infoBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:TerminalInfoSynchronous)
    }
    
    static {
      defaultInstance = new TerminalInfoSynchronous(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:TerminalInfoSynchronous)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_TerminalInfoSynchronous_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_TerminalInfoSynchronous_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\nAcore/proto/dataaccess/terminal/java/Te" +
      "rminalInfoSynchronous.proto\0326core/proto/" +
      "dataaccess/terminal/java/TerminalInfo.pr" +
      "oto\032*core/proto/common/java/OperationTyp" +
      "e.proto\"T\n\027TerminalInfoSynchronous\022\034\n\004ty" +
      "pe\030\001 \002(\0162\016.OperationType\022\033\n\004info\030\002 \002(\0132\r" +
      ".TerminalInfoB_\nBcom.navinfo.opentsp.pla" +
      "tform.location.protocol.dataaccess.termi" +
      "nalB\031LCTerminalInfoSynchronous"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_TerminalInfoSynchronous_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_TerminalInfoSynchronous_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_TerminalInfoSynchronous_descriptor,
              new java.lang.String[] { "Type", "Info", },
              com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous.class,
              com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfoSynchronous.TerminalInfoSynchronous.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.getDescriptor(),
          com.navinfo.opentsp.platform.location.protocol.common.LCOperationType.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
