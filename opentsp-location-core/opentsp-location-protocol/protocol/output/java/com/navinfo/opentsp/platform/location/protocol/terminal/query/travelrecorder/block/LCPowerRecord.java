// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/terminal/query/travelrecorder/block/java/PowerRecord.proto

package com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block;

public final class LCPowerRecord {
  private LCPowerRecord() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface PowerRecordOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int64 occurDate = 1;
    boolean hasOccurDate();
    long getOccurDate();
    
    // required .PowerEventType types = 2;
    boolean hasTypes();
    com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPowerEventType.PowerEventType getTypes();
  }
  public static final class PowerRecord extends
      com.google.protobuf.GeneratedMessage
      implements PowerRecordOrBuilder {
    // Use PowerRecord.newBuilder() to construct.
    private PowerRecord(Builder builder) {
      super(builder);
    }
    private PowerRecord(boolean noInit) {}
    
    private static final PowerRecord defaultInstance;
    public static PowerRecord getDefaultInstance() {
      return defaultInstance;
    }
    
    public PowerRecord getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.internal_static_PowerRecord_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.internal_static_PowerRecord_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required int64 occurDate = 1;
    public static final int OCCURDATE_FIELD_NUMBER = 1;
    private long occurDate_;
    public boolean hasOccurDate() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public long getOccurDate() {
      return occurDate_;
    }
    
    // required .PowerEventType types = 2;
    public static final int TYPES_FIELD_NUMBER = 2;
    private com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPowerEventType.PowerEventType types_;
    public boolean hasTypes() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPowerEventType.PowerEventType getTypes() {
      return types_;
    }
    
    private void initFields() {
      occurDate_ = 0L;
      types_ = com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPowerEventType.PowerEventType.powerOn;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasOccurDate()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasTypes()) {
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
        output.writeInt64(1, occurDate_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeEnum(2, types_.getNumber());
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
          .computeInt64Size(1, occurDate_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(2, types_.getNumber());
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
    
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord parseDelimitedFrom(
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
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord prototype) {
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
       implements com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecordOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.internal_static_PowerRecord_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.internal_static_PowerRecord_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord.newBuilder()
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
        occurDate_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000001);
        types_ = com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPowerEventType.PowerEventType.powerOn;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord.getDescriptor();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord getDefaultInstanceForType() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord.getDefaultInstance();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord build() {
        com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord buildPartial() {
        com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord result = new com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.occurDate_ = occurDate_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.types_ = types_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord) {
          return mergeFrom((com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord other) {
        if (other == com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord.getDefaultInstance()) return this;
        if (other.hasOccurDate()) {
          setOccurDate(other.getOccurDate());
        }
        if (other.hasTypes()) {
          setTypes(other.getTypes());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasOccurDate()) {
          
          return false;
        }
        if (!hasTypes()) {
          
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
              occurDate_ = input.readInt64();
              break;
            }
            case 16: {
              int rawValue = input.readEnum();
              com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPowerEventType.PowerEventType value = com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPowerEventType.PowerEventType.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(2, rawValue);
              } else {
                bitField0_ |= 0x00000002;
                types_ = value;
              }
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required int64 occurDate = 1;
      private long occurDate_ ;
      public boolean hasOccurDate() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public long getOccurDate() {
        return occurDate_;
      }
      public Builder setOccurDate(long value) {
        bitField0_ |= 0x00000001;
        occurDate_ = value;
        onChanged();
        return this;
      }
      public Builder clearOccurDate() {
        bitField0_ = (bitField0_ & ~0x00000001);
        occurDate_ = 0L;
        onChanged();
        return this;
      }
      
      // required .PowerEventType types = 2;
      private com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPowerEventType.PowerEventType types_ = com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPowerEventType.PowerEventType.powerOn;
      public boolean hasTypes() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPowerEventType.PowerEventType getTypes() {
        return types_;
      }
      public Builder setTypes(com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPowerEventType.PowerEventType value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000002;
        types_ = value;
        onChanged();
        return this;
      }
      public Builder clearTypes() {
        bitField0_ = (bitField0_ & ~0x00000002);
        types_ = com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPowerEventType.PowerEventType.powerOn;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:PowerRecord)
    }
    
    static {
      defaultInstance = new PowerRecord(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:PowerRecord)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_PowerRecord_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_PowerRecord_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\nEcore/proto/terminal/query/travelrecord" +
      "er/block/java/PowerRecord.proto\0324core/pr" +
      "oto/terminal/common/java/PowerEventType." +
      "proto\"@\n\013PowerRecord\022\021\n\toccurDate\030\001 \002(\003\022" +
      "\036\n\005types\030\002 \002(\0162\017.PowerEventTypeBc\nRcom.n" +
      "avinfo.opentsp.platform.location.protoco" +
      "l.terminal.query.travelrecorder.blockB\rL" +
      "CPowerRecord"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_PowerRecord_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_PowerRecord_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_PowerRecord_descriptor,
              new java.lang.String[] { "OccurDate", "Types", },
              com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord.class,
              com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.block.LCPowerRecord.PowerRecord.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPowerEventType.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
