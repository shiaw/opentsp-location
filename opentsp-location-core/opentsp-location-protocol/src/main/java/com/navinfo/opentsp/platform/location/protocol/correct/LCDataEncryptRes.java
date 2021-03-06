// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/correct/java/DataEncryptRes.proto

package com.navinfo.opentsp.platform.location.protocol.correct;

public final class LCDataEncryptRes {
  private LCDataEncryptRes() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface DataEncryptResOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int64 terminalIdentify = 1;
    boolean hasTerminalIdentify();
    long getTerminalIdentify();
    
    // required int32 longitude = 2;
    boolean hasLongitude();
    int getLongitude();
    
    // required int32 latitude = 3;
    boolean hasLatitude();
    int getLatitude();
    
    // required int32 serialNumber = 4;
    boolean hasSerialNumber();
    int getSerialNumber();
  }
  public static final class DataEncryptRes extends
      com.google.protobuf.GeneratedMessage
      implements DataEncryptResOrBuilder {
    // Use DataEncryptRes.newBuilder() to construct.
    private DataEncryptRes(Builder builder) {
      super(builder);
    }
    private DataEncryptRes(boolean noInit) {}
    
    private static final DataEncryptRes defaultInstance;
    public static DataEncryptRes getDefaultInstance() {
      return defaultInstance;
    }
    
    public DataEncryptRes getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return LCDataEncryptRes.internal_static_DataEncryptRes_descriptor;
    }
    
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return LCDataEncryptRes.internal_static_DataEncryptRes_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required int64 terminalIdentify = 1;
    public static final int TERMINALIDENTIFY_FIELD_NUMBER = 1;
    private long terminalIdentify_;
    public boolean hasTerminalIdentify() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public long getTerminalIdentify() {
      return terminalIdentify_;
    }
    
    // required int32 longitude = 2;
    public static final int LONGITUDE_FIELD_NUMBER = 2;
    private int longitude_;
    public boolean hasLongitude() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public int getLongitude() {
      return longitude_;
    }
    
    // required int32 latitude = 3;
    public static final int LATITUDE_FIELD_NUMBER = 3;
    private int latitude_;
    public boolean hasLatitude() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public int getLatitude() {
      return latitude_;
    }
    
    // required int32 serialNumber = 4;
    public static final int SERIALNUMBER_FIELD_NUMBER = 4;
    private int serialNumber_;
    public boolean hasSerialNumber() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public int getSerialNumber() {
      return serialNumber_;
    }
    
    private void initFields() {
      terminalIdentify_ = 0L;
      longitude_ = 0;
      latitude_ = 0;
      serialNumber_ = 0;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasTerminalIdentify()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasLongitude()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasLatitude()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasSerialNumber()) {
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
        output.writeInt64(1, terminalIdentify_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, longitude_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(3, latitude_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeInt32(4, serialNumber_);
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
          .computeInt64Size(1, terminalIdentify_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, longitude_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, latitude_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(4, serialNumber_);
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
    
    public static DataEncryptRes parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static DataEncryptRes parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static DataEncryptRes parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static DataEncryptRes parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static DataEncryptRes parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static DataEncryptRes parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static DataEncryptRes parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static DataEncryptRes parseDelimitedFrom(
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
    public static DataEncryptRes parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static DataEncryptRes parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(DataEncryptRes prototype) {
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
       implements DataEncryptResOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return LCDataEncryptRes.internal_static_DataEncryptRes_descriptor;
      }
      
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return LCDataEncryptRes.internal_static_DataEncryptRes_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.correct.LCDataEncryptRes.DataEncryptRes.newBuilder()
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
        terminalIdentify_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000001);
        longitude_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        latitude_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        serialNumber_ = 0;
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return DataEncryptRes.getDescriptor();
      }
      
      public DataEncryptRes getDefaultInstanceForType() {
        return DataEncryptRes.getDefaultInstance();
      }
      
      public DataEncryptRes build() {
        DataEncryptRes result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private DataEncryptRes buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        DataEncryptRes result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public DataEncryptRes buildPartial() {
        DataEncryptRes result = new DataEncryptRes(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.terminalIdentify_ = terminalIdentify_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.longitude_ = longitude_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.latitude_ = latitude_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.serialNumber_ = serialNumber_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof DataEncryptRes) {
          return mergeFrom((DataEncryptRes)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(DataEncryptRes other) {
        if (other == DataEncryptRes.getDefaultInstance()) return this;
        if (other.hasTerminalIdentify()) {
          setTerminalIdentify(other.getTerminalIdentify());
        }
        if (other.hasLongitude()) {
          setLongitude(other.getLongitude());
        }
        if (other.hasLatitude()) {
          setLatitude(other.getLatitude());
        }
        if (other.hasSerialNumber()) {
          setSerialNumber(other.getSerialNumber());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasTerminalIdentify()) {
          
          return false;
        }
        if (!hasLongitude()) {
          
          return false;
        }
        if (!hasLatitude()) {
          
          return false;
        }
        if (!hasSerialNumber()) {
          
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
              terminalIdentify_ = input.readInt64();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              longitude_ = input.readInt32();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              latitude_ = input.readInt32();
              break;
            }
            case 32: {
              bitField0_ |= 0x00000008;
              serialNumber_ = input.readInt32();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required int64 terminalIdentify = 1;
      private long terminalIdentify_ ;
      public boolean hasTerminalIdentify() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public long getTerminalIdentify() {
        return terminalIdentify_;
      }
      public Builder setTerminalIdentify(long value) {
        bitField0_ |= 0x00000001;
        terminalIdentify_ = value;
        onChanged();
        return this;
      }
      public Builder clearTerminalIdentify() {
        bitField0_ = (bitField0_ & ~0x00000001);
        terminalIdentify_ = 0L;
        onChanged();
        return this;
      }
      
      // required int32 longitude = 2;
      private int longitude_ ;
      public boolean hasLongitude() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public int getLongitude() {
        return longitude_;
      }
      public Builder setLongitude(int value) {
        bitField0_ |= 0x00000002;
        longitude_ = value;
        onChanged();
        return this;
      }
      public Builder clearLongitude() {
        bitField0_ = (bitField0_ & ~0x00000002);
        longitude_ = 0;
        onChanged();
        return this;
      }
      
      // required int32 latitude = 3;
      private int latitude_ ;
      public boolean hasLatitude() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public int getLatitude() {
        return latitude_;
      }
      public Builder setLatitude(int value) {
        bitField0_ |= 0x00000004;
        latitude_ = value;
        onChanged();
        return this;
      }
      public Builder clearLatitude() {
        bitField0_ = (bitField0_ & ~0x00000004);
        latitude_ = 0;
        onChanged();
        return this;
      }
      
      // required int32 serialNumber = 4;
      private int serialNumber_ ;
      public boolean hasSerialNumber() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public int getSerialNumber() {
        return serialNumber_;
      }
      public Builder setSerialNumber(int value) {
        bitField0_ |= 0x00000008;
        serialNumber_ = value;
        onChanged();
        return this;
      }
      public Builder clearSerialNumber() {
        bitField0_ = (bitField0_ & ~0x00000008);
        serialNumber_ = 0;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:DataEncryptRes)
    }
    
    static {
      defaultInstance = new DataEncryptRes(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:DataEncryptRes)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_DataEncryptRes_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_DataEncryptRes_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n,core/proto/correct/java/DataEncryptRes" +
      ".proto\"e\n\016DataEncryptRes\022\030\n\020terminalIden" +
      "tify\030\001 \002(\003\022\021\n\tlongitude\030\002 \002(\005\022\020\n\010latitud" +
      "e\030\003 \002(\005\022\024\n\014serialNumber\030\004 \002(\005BJ\n6com.nav" +
      "info.opentsp.platform.location.protocol." +
      "correctB\020LCDataEncryptRes"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_DataEncryptRes_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_DataEncryptRes_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_DataEncryptRes_descriptor,
              new String[] { "TerminalIdentify", "Longitude", "Latitude", "SerialNumber", },
              DataEncryptRes.class,
              DataEncryptRes.Builder.class);
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
