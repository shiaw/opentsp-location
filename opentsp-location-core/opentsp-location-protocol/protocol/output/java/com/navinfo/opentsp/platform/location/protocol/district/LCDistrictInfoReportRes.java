// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/district/java/DistrictInfoReportRes.proto

package com.navinfo.opentsp.platform.location.protocol.district;

public final class LCDistrictInfoReportRes {
  private LCDistrictInfoReportRes() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface DistrictInfoReportResOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int32 serialNumber = 1;
    boolean hasSerialNumber();
    int getSerialNumber();
    
    // required .DistrictInfoReportRes.ReportResult results = 2;
    boolean hasResults();
    com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.ReportResult getResults();
  }
  public static final class DistrictInfoReportRes extends
      com.google.protobuf.GeneratedMessage
      implements DistrictInfoReportResOrBuilder {
    // Use DistrictInfoReportRes.newBuilder() to construct.
    private DistrictInfoReportRes(Builder builder) {
      super(builder);
    }
    private DistrictInfoReportRes(boolean noInit) {}
    
    private static final DistrictInfoReportRes defaultInstance;
    public static DistrictInfoReportRes getDefaultInstance() {
      return defaultInstance;
    }
    
    public DistrictInfoReportRes getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.internal_static_DistrictInfoReportRes_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.internal_static_DistrictInfoReportRes_fieldAccessorTable;
    }
    
    public enum ReportResult
        implements com.google.protobuf.ProtocolMessageEnum {
      success(0, 1),
      illegalDistrict(1, 2),
      illegalNodeCode(2, 3),
      ;
      
      public static final int success_VALUE = 1;
      public static final int illegalDistrict_VALUE = 2;
      public static final int illegalNodeCode_VALUE = 3;
      
      
      public final int getNumber() { return value; }
      
      public static ReportResult valueOf(int value) {
        switch (value) {
          case 1: return success;
          case 2: return illegalDistrict;
          case 3: return illegalNodeCode;
          default: return null;
        }
      }
      
      public static com.google.protobuf.Internal.EnumLiteMap<ReportResult>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static com.google.protobuf.Internal.EnumLiteMap<ReportResult>
          internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<ReportResult>() {
              public ReportResult findValueByNumber(int number) {
                return ReportResult.valueOf(number);
              }
            };
      
      public final com.google.protobuf.Descriptors.EnumValueDescriptor
          getValueDescriptor() {
        return getDescriptor().getValues().get(index);
      }
      public final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptorForType() {
        return getDescriptor();
      }
      public static final com.google.protobuf.Descriptors.EnumDescriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.getDescriptor().getEnumTypes().get(0);
      }
      
      private static final ReportResult[] VALUES = {
        success, illegalDistrict, illegalNodeCode, 
      };
      
      public static ReportResult valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new java.lang.IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        return VALUES[desc.getIndex()];
      }
      
      private final int index;
      private final int value;
      
      private ReportResult(int index, int value) {
        this.index = index;
        this.value = value;
      }
      
      // @@protoc_insertion_point(enum_scope:DistrictInfoReportRes.ReportResult)
    }
    
    private int bitField0_;
    // required int32 serialNumber = 1;
    public static final int SERIALNUMBER_FIELD_NUMBER = 1;
    private int serialNumber_;
    public boolean hasSerialNumber() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public int getSerialNumber() {
      return serialNumber_;
    }
    
    // required .DistrictInfoReportRes.ReportResult results = 2;
    public static final int RESULTS_FIELD_NUMBER = 2;
    private com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.ReportResult results_;
    public boolean hasResults() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.ReportResult getResults() {
      return results_;
    }
    
    private void initFields() {
      serialNumber_ = 0;
      results_ = com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.ReportResult.success;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasSerialNumber()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasResults()) {
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
        output.writeInt32(1, serialNumber_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeEnum(2, results_.getNumber());
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
          .computeInt32Size(1, serialNumber_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(2, results_.getNumber());
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
    
    public static com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes parseDelimitedFrom(
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
    public static com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes prototype) {
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
       implements com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportResOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.internal_static_DistrictInfoReportRes_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.internal_static_DistrictInfoReportRes_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.newBuilder()
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
        serialNumber_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        results_ = com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.ReportResult.success;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.getDescriptor();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes getDefaultInstanceForType() {
        return com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.getDefaultInstance();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes build() {
        com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes buildPartial() {
        com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes result = new com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.serialNumber_ = serialNumber_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.results_ = results_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes) {
          return mergeFrom((com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes other) {
        if (other == com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.getDefaultInstance()) return this;
        if (other.hasSerialNumber()) {
          setSerialNumber(other.getSerialNumber());
        }
        if (other.hasResults()) {
          setResults(other.getResults());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasSerialNumber()) {
          
          return false;
        }
        if (!hasResults()) {
          
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
              serialNumber_ = input.readInt32();
              break;
            }
            case 16: {
              int rawValue = input.readEnum();
              com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.ReportResult value = com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.ReportResult.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(2, rawValue);
              } else {
                bitField0_ |= 0x00000002;
                results_ = value;
              }
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required int32 serialNumber = 1;
      private int serialNumber_ ;
      public boolean hasSerialNumber() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public int getSerialNumber() {
        return serialNumber_;
      }
      public Builder setSerialNumber(int value) {
        bitField0_ |= 0x00000001;
        serialNumber_ = value;
        onChanged();
        return this;
      }
      public Builder clearSerialNumber() {
        bitField0_ = (bitField0_ & ~0x00000001);
        serialNumber_ = 0;
        onChanged();
        return this;
      }
      
      // required .DistrictInfoReportRes.ReportResult results = 2;
      private com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.ReportResult results_ = com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.ReportResult.success;
      public boolean hasResults() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.ReportResult getResults() {
        return results_;
      }
      public Builder setResults(com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.ReportResult value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000002;
        results_ = value;
        onChanged();
        return this;
      }
      public Builder clearResults() {
        bitField0_ = (bitField0_ & ~0x00000002);
        results_ = com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.ReportResult.success;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:DistrictInfoReportRes)
    }
    
    static {
      defaultInstance = new DistrictInfoReportRes(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:DistrictInfoReportRes)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_DistrictInfoReportRes_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_DistrictInfoReportRes_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n4core/proto/district/java/DistrictInfoR" +
      "eportRes.proto\"\252\001\n\025DistrictInfoReportRes" +
      "\022\024\n\014serialNumber\030\001 \002(\005\0224\n\007results\030\002 \002(\0162" +
      "#.DistrictInfoReportRes.ReportResult\"E\n\014" +
      "ReportResult\022\013\n\007success\020\001\022\023\n\017illegalDist" +
      "rict\020\002\022\023\n\017illegalNodeCode\020\003BR\n7com.navin" +
      "fo.opentsp.platform.location.protocol.di" +
      "strictB\027LCDistrictInfoReportRes"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_DistrictInfoReportRes_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_DistrictInfoReportRes_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_DistrictInfoReportRes_descriptor,
              new java.lang.String[] { "SerialNumber", "Results", },
              com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.class,
              com.navinfo.opentsp.platform.location.protocol.district.LCDistrictInfoReportRes.DistrictInfoReportRes.Builder.class);
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