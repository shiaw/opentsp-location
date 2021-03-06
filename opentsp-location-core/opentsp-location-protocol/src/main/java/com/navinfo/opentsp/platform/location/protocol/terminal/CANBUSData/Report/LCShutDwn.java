// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/terminal/CANBUSData/Report/ShutDwn.proto

package com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.Report;

public final class LCShutDwn {
  private LCShutDwn() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface ShutDwnOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // optional int32 startLamp = 1;
    boolean hasStartLamp();
    int getStartLamp();
    
    // optional int32 engineProtectSystem = 2;
    boolean hasEngineProtectSystem();
    int getEngineProtectSystem();
  }
  public static final class ShutDwn extends
      com.google.protobuf.GeneratedMessage
      implements ShutDwnOrBuilder {
    // Use ShutDwn.newBuilder() to construct.
    private ShutDwn(Builder builder) {
      super(builder);
    }
    private ShutDwn(boolean noInit) {}
    
    private static final ShutDwn defaultInstance;
    public static ShutDwn getDefaultInstance() {
      return defaultInstance;
    }
    
    public ShutDwn getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return LCShutDwn.internal_static_ShutDwn_descriptor;
    }
    
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return LCShutDwn.internal_static_ShutDwn_fieldAccessorTable;
    }
    
    private int bitField0_;
    // optional int32 startLamp = 1;
    public static final int STARTLAMP_FIELD_NUMBER = 1;
    private int startLamp_;
    public boolean hasStartLamp() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public int getStartLamp() {
      return startLamp_;
    }
    
    // optional int32 engineProtectSystem = 2;
    public static final int ENGINEPROTECTSYSTEM_FIELD_NUMBER = 2;
    private int engineProtectSystem_;
    public boolean hasEngineProtectSystem() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public int getEngineProtectSystem() {
      return engineProtectSystem_;
    }
    
    private void initFields() {
      startLamp_ = 0;
      engineProtectSystem_ = 0;
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
        output.writeInt32(1, startLamp_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, engineProtectSystem_);
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
          .computeInt32Size(1, startLamp_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, engineProtectSystem_);
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
    
    public static ShutDwn parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static ShutDwn parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static ShutDwn parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static ShutDwn parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static ShutDwn parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static ShutDwn parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static ShutDwn parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static ShutDwn parseDelimitedFrom(
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
    public static ShutDwn parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static ShutDwn parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(ShutDwn prototype) {
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
       implements ShutDwnOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return LCShutDwn.internal_static_ShutDwn_descriptor;
      }
      
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return LCShutDwn.internal_static_ShutDwn_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.Report.LCShutDwn.ShutDwn.newBuilder()
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
        startLamp_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        engineProtectSystem_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ShutDwn.getDescriptor();
      }
      
      public ShutDwn getDefaultInstanceForType() {
        return ShutDwn.getDefaultInstance();
      }
      
      public ShutDwn build() {
        ShutDwn result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private ShutDwn buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        ShutDwn result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public ShutDwn buildPartial() {
        ShutDwn result = new ShutDwn(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.startLamp_ = startLamp_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.engineProtectSystem_ = engineProtectSystem_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ShutDwn) {
          return mergeFrom((ShutDwn)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(ShutDwn other) {
        if (other == ShutDwn.getDefaultInstance()) return this;
        if (other.hasStartLamp()) {
          setStartLamp(other.getStartLamp());
        }
        if (other.hasEngineProtectSystem()) {
          setEngineProtectSystem(other.getEngineProtectSystem());
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
            case 8: {
              bitField0_ |= 0x00000001;
              startLamp_ = input.readInt32();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              engineProtectSystem_ = input.readInt32();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // optional int32 startLamp = 1;
      private int startLamp_ ;
      public boolean hasStartLamp() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public int getStartLamp() {
        return startLamp_;
      }
      public Builder setStartLamp(int value) {
        bitField0_ |= 0x00000001;
        startLamp_ = value;
        onChanged();
        return this;
      }
      public Builder clearStartLamp() {
        bitField0_ = (bitField0_ & ~0x00000001);
        startLamp_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 engineProtectSystem = 2;
      private int engineProtectSystem_ ;
      public boolean hasEngineProtectSystem() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public int getEngineProtectSystem() {
        return engineProtectSystem_;
      }
      public Builder setEngineProtectSystem(int value) {
        bitField0_ |= 0x00000002;
        engineProtectSystem_ = value;
        onChanged();
        return this;
      }
      public Builder clearEngineProtectSystem() {
        bitField0_ = (bitField0_ & ~0x00000002);
        engineProtectSystem_ = 0;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:ShutDwn)
    }
    
    static {
      defaultInstance = new ShutDwn(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:ShutDwn)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_ShutDwn_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_ShutDwn_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n3core/proto/terminal/CANBUSData/Report/" +
      "ShutDwn.proto\"9\n\007ShutDwn\022\021\n\tstartLamp\030\001 " +
      "\001(\005\022\033\n\023engineProtectSystem\030\002 \001(\005BV\nIcom." +
      "navinfo.opentsp.platform.location.protoc" +
      "ol.terminal.CANBUSData.ReportB\tLCShutDwn"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_ShutDwn_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_ShutDwn_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_ShutDwn_descriptor,
              new String[] { "StartLamp", "EngineProtectSystem", },
              ShutDwn.class,
              ShutDwn.Builder.class);
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
