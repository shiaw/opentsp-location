// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/webservice/originaldata/java/LimitSpeedRecord.proto

package com.navinfo.opentsp.platform.location.protocol.webservice.originaldata;

public final class LCLimitSpeedRecord {
  private LCLimitSpeedRecord() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface LimitSpeedRecordOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int64 terminalId = 1;
    boolean hasTerminalId();
    long getTerminalId();
    
    // required int64 areaIdentify = 2;
    boolean hasAreaIdentify();
    long getAreaIdentify();
    
    // required int64 limitDate = 3;
    boolean hasLimitDate();
    long getLimitDate();
    
    // required int32 limitSpeed = 4;
    boolean hasLimitSpeed();
    int getLimitSpeed();
    
    // required int32 sign = 5;
    boolean hasSign();
    int getSign();
  }
  public static final class LimitSpeedRecord extends
      com.google.protobuf.GeneratedMessage
      implements LimitSpeedRecordOrBuilder {
    // Use LimitSpeedRecord.newBuilder() to construct.
    private LimitSpeedRecord(Builder builder) {
      super(builder);
    }
    private LimitSpeedRecord(boolean noInit) {}
    
    private static final LimitSpeedRecord defaultInstance;
    public static LimitSpeedRecord getDefaultInstance() {
      return defaultInstance;
    }
    
    public LimitSpeedRecord getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.internal_static_LimitSpeedRecord_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.internal_static_LimitSpeedRecord_fieldAccessorTable;
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
    
    // required int64 areaIdentify = 2;
    public static final int AREAIDENTIFY_FIELD_NUMBER = 2;
    private long areaIdentify_;
    public boolean hasAreaIdentify() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public long getAreaIdentify() {
      return areaIdentify_;
    }
    
    // required int64 limitDate = 3;
    public static final int LIMITDATE_FIELD_NUMBER = 3;
    private long limitDate_;
    public boolean hasLimitDate() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public long getLimitDate() {
      return limitDate_;
    }
    
    // required int32 limitSpeed = 4;
    public static final int LIMITSPEED_FIELD_NUMBER = 4;
    private int limitSpeed_;
    public boolean hasLimitSpeed() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public int getLimitSpeed() {
      return limitSpeed_;
    }
    
    // required int32 sign = 5;
    public static final int SIGN_FIELD_NUMBER = 5;
    private int sign_;
    public boolean hasSign() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public int getSign() {
      return sign_;
    }
    
    private void initFields() {
      terminalId_ = 0L;
      areaIdentify_ = 0L;
      limitDate_ = 0L;
      limitSpeed_ = 0;
      sign_ = 0;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasTerminalId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasAreaIdentify()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasLimitDate()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasLimitSpeed()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasSign()) {
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
        output.writeInt64(2, areaIdentify_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt64(3, limitDate_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeInt32(4, limitSpeed_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeInt32(5, sign_);
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
          .computeInt64Size(2, areaIdentify_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(3, limitDate_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(4, limitSpeed_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(5, sign_);
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
    
    public static com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord parseDelimitedFrom(
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
    public static com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord prototype) {
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
       implements com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecordOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.internal_static_LimitSpeedRecord_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.internal_static_LimitSpeedRecord_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord.newBuilder()
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
        areaIdentify_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000002);
        limitDate_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000004);
        limitSpeed_ = 0;
        bitField0_ = (bitField0_ & ~0x00000008);
        sign_ = 0;
        bitField0_ = (bitField0_ & ~0x00000010);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord.getDescriptor();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord getDefaultInstanceForType() {
        return com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord.getDefaultInstance();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord build() {
        com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord buildPartial() {
        com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord result = new com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.terminalId_ = terminalId_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.areaIdentify_ = areaIdentify_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.limitDate_ = limitDate_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.limitSpeed_ = limitSpeed_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.sign_ = sign_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord) {
          return mergeFrom((com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord other) {
        if (other == com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord.getDefaultInstance()) return this;
        if (other.hasTerminalId()) {
          setTerminalId(other.getTerminalId());
        }
        if (other.hasAreaIdentify()) {
          setAreaIdentify(other.getAreaIdentify());
        }
        if (other.hasLimitDate()) {
          setLimitDate(other.getLimitDate());
        }
        if (other.hasLimitSpeed()) {
          setLimitSpeed(other.getLimitSpeed());
        }
        if (other.hasSign()) {
          setSign(other.getSign());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasTerminalId()) {
          
          return false;
        }
        if (!hasAreaIdentify()) {
          
          return false;
        }
        if (!hasLimitDate()) {
          
          return false;
        }
        if (!hasLimitSpeed()) {
          
          return false;
        }
        if (!hasSign()) {
          
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
              areaIdentify_ = input.readInt64();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              limitDate_ = input.readInt64();
              break;
            }
            case 32: {
              bitField0_ |= 0x00000008;
              limitSpeed_ = input.readInt32();
              break;
            }
            case 40: {
              bitField0_ |= 0x00000010;
              sign_ = input.readInt32();
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
      
      // required int64 areaIdentify = 2;
      private long areaIdentify_ ;
      public boolean hasAreaIdentify() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public long getAreaIdentify() {
        return areaIdentify_;
      }
      public Builder setAreaIdentify(long value) {
        bitField0_ |= 0x00000002;
        areaIdentify_ = value;
        onChanged();
        return this;
      }
      public Builder clearAreaIdentify() {
        bitField0_ = (bitField0_ & ~0x00000002);
        areaIdentify_ = 0L;
        onChanged();
        return this;
      }
      
      // required int64 limitDate = 3;
      private long limitDate_ ;
      public boolean hasLimitDate() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public long getLimitDate() {
        return limitDate_;
      }
      public Builder setLimitDate(long value) {
        bitField0_ |= 0x00000004;
        limitDate_ = value;
        onChanged();
        return this;
      }
      public Builder clearLimitDate() {
        bitField0_ = (bitField0_ & ~0x00000004);
        limitDate_ = 0L;
        onChanged();
        return this;
      }
      
      // required int32 limitSpeed = 4;
      private int limitSpeed_ ;
      public boolean hasLimitSpeed() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public int getLimitSpeed() {
        return limitSpeed_;
      }
      public Builder setLimitSpeed(int value) {
        bitField0_ |= 0x00000008;
        limitSpeed_ = value;
        onChanged();
        return this;
      }
      public Builder clearLimitSpeed() {
        bitField0_ = (bitField0_ & ~0x00000008);
        limitSpeed_ = 0;
        onChanged();
        return this;
      }
      
      // required int32 sign = 5;
      private int sign_ ;
      public boolean hasSign() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public int getSign() {
        return sign_;
      }
      public Builder setSign(int value) {
        bitField0_ |= 0x00000010;
        sign_ = value;
        onChanged();
        return this;
      }
      public Builder clearSign() {
        bitField0_ = (bitField0_ & ~0x00000010);
        sign_ = 0;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:LimitSpeedRecord)
    }
    
    static {
      defaultInstance = new LimitSpeedRecord(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:LimitSpeedRecord)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_LimitSpeedRecord_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_LimitSpeedRecord_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n>core/proto/webservice/originaldata/jav" +
      "a/LimitSpeedRecord.proto\"q\n\020LimitSpeedRe" +
      "cord\022\022\n\nterminalId\030\001 \002(\003\022\024\n\014areaIdentify" +
      "\030\002 \002(\003\022\021\n\tlimitDate\030\003 \002(\003\022\022\n\nlimitSpeed\030" +
      "\004 \002(\005\022\014\n\004sign\030\005 \002(\005B\\\nFcom.navinfo.opent" +
      "sp.platform.location.protocol.webservice" +
      ".originaldataB\022LCLimitSpeedRecord"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_LimitSpeedRecord_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_LimitSpeedRecord_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_LimitSpeedRecord_descriptor,
              new java.lang.String[] { "TerminalId", "AreaIdentify", "LimitDate", "LimitSpeed", "Sign", },
              com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord.class,
              com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCLimitSpeedRecord.LimitSpeedRecord.Builder.class);
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
