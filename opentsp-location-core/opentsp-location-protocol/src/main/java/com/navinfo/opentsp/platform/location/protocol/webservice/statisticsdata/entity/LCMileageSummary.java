// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/webservice/statisticsdata/entity/MileageSummary.proto

package com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity;

public final class LCMileageSummary {
  private LCMileageSummary() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface MileageSummaryOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int64 terminalID = 1;
    boolean hasTerminalID();
    long getTerminalID();
    
    // optional int32 recordsTotal = 12;
    boolean hasRecordsTotal();
    int getRecordsTotal();
    
    // optional int32 calculatedAT = 13;
    boolean hasCalculatedAT();
    int getCalculatedAT();
    
    // optional int64 beginMileage = 14;
    boolean hasBeginMileage();
    long getBeginMileage();
    
    // optional int64 endMileage = 3;
    boolean hasEndMileage();
    long getEndMileage();
    
    // optional int64 mileage = 4;
    boolean hasMileage();
    long getMileage();
    
    // optional int32 staticDate = 5;
    boolean hasStaticDate();
    int getStaticDate();
    
    // optional int64 startDate = 6;
    boolean hasStartDate();
    long getStartDate();
    
    // optional int64 endDate = 7;
    boolean hasEndDate();
    long getEndDate();
    
    // optional int32 beginLat = 8;
    boolean hasBeginLat();
    int getBeginLat();
    
    // optional int32 beginLng = 9;
    boolean hasBeginLng();
    int getBeginLng();
    
    // optional int32 endLat = 10;
    boolean hasEndLat();
    int getEndLat();
    
    // optional int32 endLng = 11;
    boolean hasEndLng();
    int getEndLng();
  }
  public static final class MileageSummary extends
      com.google.protobuf.GeneratedMessage
      implements MileageSummaryOrBuilder {
    // Use MileageSummary.newBuilder() to construct.
    private MileageSummary(Builder builder) {
      super(builder);
    }
    private MileageSummary(boolean noInit) {}
    
    private static final MileageSummary defaultInstance;
    public static MileageSummary getDefaultInstance() {
      return defaultInstance;
    }
    
    public MileageSummary getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return LCMileageSummary.internal_static_MileageSummary_descriptor;
    }
    
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return LCMileageSummary.internal_static_MileageSummary_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required int64 terminalID = 1;
    public static final int TERMINALID_FIELD_NUMBER = 1;
    private long terminalID_;
    public boolean hasTerminalID() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public long getTerminalID() {
      return terminalID_;
    }
    
    // optional int32 recordsTotal = 12;
    public static final int RECORDSTOTAL_FIELD_NUMBER = 12;
    private int recordsTotal_;
    public boolean hasRecordsTotal() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public int getRecordsTotal() {
      return recordsTotal_;
    }
    
    // optional int32 calculatedAT = 13;
    public static final int CALCULATEDAT_FIELD_NUMBER = 13;
    private int calculatedAT_;
    public boolean hasCalculatedAT() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public int getCalculatedAT() {
      return calculatedAT_;
    }
    
    // optional int64 beginMileage = 14;
    public static final int BEGINMILEAGE_FIELD_NUMBER = 14;
    private long beginMileage_;
    public boolean hasBeginMileage() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public long getBeginMileage() {
      return beginMileage_;
    }
    
    // optional int64 endMileage = 3;
    public static final int ENDMILEAGE_FIELD_NUMBER = 3;
    private long endMileage_;
    public boolean hasEndMileage() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public long getEndMileage() {
      return endMileage_;
    }
    
    // optional int64 mileage = 4;
    public static final int MILEAGE_FIELD_NUMBER = 4;
    private long mileage_;
    public boolean hasMileage() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    public long getMileage() {
      return mileage_;
    }
    
    // optional int32 staticDate = 5;
    public static final int STATICDATE_FIELD_NUMBER = 5;
    private int staticDate_;
    public boolean hasStaticDate() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }
    public int getStaticDate() {
      return staticDate_;
    }
    
    // optional int64 startDate = 6;
    public static final int STARTDATE_FIELD_NUMBER = 6;
    private long startDate_;
    public boolean hasStartDate() {
      return ((bitField0_ & 0x00000080) == 0x00000080);
    }
    public long getStartDate() {
      return startDate_;
    }
    
    // optional int64 endDate = 7;
    public static final int ENDDATE_FIELD_NUMBER = 7;
    private long endDate_;
    public boolean hasEndDate() {
      return ((bitField0_ & 0x00000100) == 0x00000100);
    }
    public long getEndDate() {
      return endDate_;
    }
    
    // optional int32 beginLat = 8;
    public static final int BEGINLAT_FIELD_NUMBER = 8;
    private int beginLat_;
    public boolean hasBeginLat() {
      return ((bitField0_ & 0x00000200) == 0x00000200);
    }
    public int getBeginLat() {
      return beginLat_;
    }
    
    // optional int32 beginLng = 9;
    public static final int BEGINLNG_FIELD_NUMBER = 9;
    private int beginLng_;
    public boolean hasBeginLng() {
      return ((bitField0_ & 0x00000400) == 0x00000400);
    }
    public int getBeginLng() {
      return beginLng_;
    }
    
    // optional int32 endLat = 10;
    public static final int ENDLAT_FIELD_NUMBER = 10;
    private int endLat_;
    public boolean hasEndLat() {
      return ((bitField0_ & 0x00000800) == 0x00000800);
    }
    public int getEndLat() {
      return endLat_;
    }
    
    // optional int32 endLng = 11;
    public static final int ENDLNG_FIELD_NUMBER = 11;
    private int endLng_;
    public boolean hasEndLng() {
      return ((bitField0_ & 0x00001000) == 0x00001000);
    }
    public int getEndLng() {
      return endLng_;
    }
    
    private void initFields() {
      terminalID_ = 0L;
      recordsTotal_ = 0;
      calculatedAT_ = 0;
      beginMileage_ = 0L;
      endMileage_ = 0L;
      mileage_ = 0L;
      staticDate_ = 0;
      startDate_ = 0L;
      endDate_ = 0L;
      beginLat_ = 0;
      beginLng_ = 0;
      endLat_ = 0;
      endLng_ = 0;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasTerminalID()) {
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
        output.writeInt64(1, terminalID_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeInt64(3, endMileage_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeInt64(4, mileage_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeInt32(5, staticDate_);
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        output.writeInt64(6, startDate_);
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        output.writeInt64(7, endDate_);
      }
      if (((bitField0_ & 0x00000200) == 0x00000200)) {
        output.writeInt32(8, beginLat_);
      }
      if (((bitField0_ & 0x00000400) == 0x00000400)) {
        output.writeInt32(9, beginLng_);
      }
      if (((bitField0_ & 0x00000800) == 0x00000800)) {
        output.writeInt32(10, endLat_);
      }
      if (((bitField0_ & 0x00001000) == 0x00001000)) {
        output.writeInt32(11, endLng_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(12, recordsTotal_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(13, calculatedAT_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeInt64(14, beginMileage_);
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
          .computeInt64Size(1, terminalID_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(3, endMileage_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(4, mileage_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(5, staticDate_);
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(6, startDate_);
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(7, endDate_);
      }
      if (((bitField0_ & 0x00000200) == 0x00000200)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(8, beginLat_);
      }
      if (((bitField0_ & 0x00000400) == 0x00000400)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(9, beginLng_);
      }
      if (((bitField0_ & 0x00000800) == 0x00000800)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(10, endLat_);
      }
      if (((bitField0_ & 0x00001000) == 0x00001000)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(11, endLng_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(12, recordsTotal_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(13, calculatedAT_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(14, beginMileage_);
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
    
    public static MileageSummary parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static MileageSummary parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static MileageSummary parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static MileageSummary parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static MileageSummary parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static MileageSummary parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static MileageSummary parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static MileageSummary parseDelimitedFrom(
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
    public static MileageSummary parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static MileageSummary parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(MileageSummary prototype) {
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
       implements MileageSummaryOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return LCMileageSummary.internal_static_MileageSummary_descriptor;
      }
      
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return LCMileageSummary.internal_static_MileageSummary_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCMileageSummary.MileageSummary.newBuilder()
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
        terminalID_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000001);
        recordsTotal_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        calculatedAT_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        beginMileage_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000008);
        endMileage_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000010);
        mileage_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000020);
        staticDate_ = 0;
        bitField0_ = (bitField0_ & ~0x00000040);
        startDate_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000080);
        endDate_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000100);
        beginLat_ = 0;
        bitField0_ = (bitField0_ & ~0x00000200);
        beginLng_ = 0;
        bitField0_ = (bitField0_ & ~0x00000400);
        endLat_ = 0;
        bitField0_ = (bitField0_ & ~0x00000800);
        endLng_ = 0;
        bitField0_ = (bitField0_ & ~0x00001000);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return MileageSummary.getDescriptor();
      }
      
      public MileageSummary getDefaultInstanceForType() {
        return MileageSummary.getDefaultInstance();
      }
      
      public MileageSummary build() {
        MileageSummary result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private MileageSummary buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        MileageSummary result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public MileageSummary buildPartial() {
        MileageSummary result = new MileageSummary(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.terminalID_ = terminalID_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.recordsTotal_ = recordsTotal_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.calculatedAT_ = calculatedAT_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.beginMileage_ = beginMileage_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.endMileage_ = endMileage_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.mileage_ = mileage_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000040;
        }
        result.staticDate_ = staticDate_;
        if (((from_bitField0_ & 0x00000080) == 0x00000080)) {
          to_bitField0_ |= 0x00000080;
        }
        result.startDate_ = startDate_;
        if (((from_bitField0_ & 0x00000100) == 0x00000100)) {
          to_bitField0_ |= 0x00000100;
        }
        result.endDate_ = endDate_;
        if (((from_bitField0_ & 0x00000200) == 0x00000200)) {
          to_bitField0_ |= 0x00000200;
        }
        result.beginLat_ = beginLat_;
        if (((from_bitField0_ & 0x00000400) == 0x00000400)) {
          to_bitField0_ |= 0x00000400;
        }
        result.beginLng_ = beginLng_;
        if (((from_bitField0_ & 0x00000800) == 0x00000800)) {
          to_bitField0_ |= 0x00000800;
        }
        result.endLat_ = endLat_;
        if (((from_bitField0_ & 0x00001000) == 0x00001000)) {
          to_bitField0_ |= 0x00001000;
        }
        result.endLng_ = endLng_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof MileageSummary) {
          return mergeFrom((MileageSummary)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(MileageSummary other) {
        if (other == MileageSummary.getDefaultInstance()) return this;
        if (other.hasTerminalID()) {
          setTerminalID(other.getTerminalID());
        }
        if (other.hasRecordsTotal()) {
          setRecordsTotal(other.getRecordsTotal());
        }
        if (other.hasCalculatedAT()) {
          setCalculatedAT(other.getCalculatedAT());
        }
        if (other.hasBeginMileage()) {
          setBeginMileage(other.getBeginMileage());
        }
        if (other.hasEndMileage()) {
          setEndMileage(other.getEndMileage());
        }
        if (other.hasMileage()) {
          setMileage(other.getMileage());
        }
        if (other.hasStaticDate()) {
          setStaticDate(other.getStaticDate());
        }
        if (other.hasStartDate()) {
          setStartDate(other.getStartDate());
        }
        if (other.hasEndDate()) {
          setEndDate(other.getEndDate());
        }
        if (other.hasBeginLat()) {
          setBeginLat(other.getBeginLat());
        }
        if (other.hasBeginLng()) {
          setBeginLng(other.getBeginLng());
        }
        if (other.hasEndLat()) {
          setEndLat(other.getEndLat());
        }
        if (other.hasEndLng()) {
          setEndLng(other.getEndLng());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasTerminalID()) {
          
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
              terminalID_ = input.readInt64();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000010;
              endMileage_ = input.readInt64();
              break;
            }
            case 32: {
              bitField0_ |= 0x00000020;
              mileage_ = input.readInt64();
              break;
            }
            case 40: {
              bitField0_ |= 0x00000040;
              staticDate_ = input.readInt32();
              break;
            }
            case 48: {
              bitField0_ |= 0x00000080;
              startDate_ = input.readInt64();
              break;
            }
            case 56: {
              bitField0_ |= 0x00000100;
              endDate_ = input.readInt64();
              break;
            }
            case 64: {
              bitField0_ |= 0x00000200;
              beginLat_ = input.readInt32();
              break;
            }
            case 72: {
              bitField0_ |= 0x00000400;
              beginLng_ = input.readInt32();
              break;
            }
            case 80: {
              bitField0_ |= 0x00000800;
              endLat_ = input.readInt32();
              break;
            }
            case 88: {
              bitField0_ |= 0x00001000;
              endLng_ = input.readInt32();
              break;
            }
            case 96: {
              bitField0_ |= 0x00000002;
              recordsTotal_ = input.readInt32();
              break;
            }
            case 104: {
              bitField0_ |= 0x00000004;
              calculatedAT_ = input.readInt32();
              break;
            }
            case 112: {
              bitField0_ |= 0x00000008;
              beginMileage_ = input.readInt64();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required int64 terminalID = 1;
      private long terminalID_ ;
      public boolean hasTerminalID() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public long getTerminalID() {
        return terminalID_;
      }
      public Builder setTerminalID(long value) {
        bitField0_ |= 0x00000001;
        terminalID_ = value;
        onChanged();
        return this;
      }
      public Builder clearTerminalID() {
        bitField0_ = (bitField0_ & ~0x00000001);
        terminalID_ = 0L;
        onChanged();
        return this;
      }
      
      // optional int32 recordsTotal = 12;
      private int recordsTotal_ ;
      public boolean hasRecordsTotal() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public int getRecordsTotal() {
        return recordsTotal_;
      }
      public Builder setRecordsTotal(int value) {
        bitField0_ |= 0x00000002;
        recordsTotal_ = value;
        onChanged();
        return this;
      }
      public Builder clearRecordsTotal() {
        bitField0_ = (bitField0_ & ~0x00000002);
        recordsTotal_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 calculatedAT = 13;
      private int calculatedAT_ ;
      public boolean hasCalculatedAT() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public int getCalculatedAT() {
        return calculatedAT_;
      }
      public Builder setCalculatedAT(int value) {
        bitField0_ |= 0x00000004;
        calculatedAT_ = value;
        onChanged();
        return this;
      }
      public Builder clearCalculatedAT() {
        bitField0_ = (bitField0_ & ~0x00000004);
        calculatedAT_ = 0;
        onChanged();
        return this;
      }
      
      // optional int64 beginMileage = 14;
      private long beginMileage_ ;
      public boolean hasBeginMileage() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public long getBeginMileage() {
        return beginMileage_;
      }
      public Builder setBeginMileage(long value) {
        bitField0_ |= 0x00000008;
        beginMileage_ = value;
        onChanged();
        return this;
      }
      public Builder clearBeginMileage() {
        bitField0_ = (bitField0_ & ~0x00000008);
        beginMileage_ = 0L;
        onChanged();
        return this;
      }
      
      // optional int64 endMileage = 3;
      private long endMileage_ ;
      public boolean hasEndMileage() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public long getEndMileage() {
        return endMileage_;
      }
      public Builder setEndMileage(long value) {
        bitField0_ |= 0x00000010;
        endMileage_ = value;
        onChanged();
        return this;
      }
      public Builder clearEndMileage() {
        bitField0_ = (bitField0_ & ~0x00000010);
        endMileage_ = 0L;
        onChanged();
        return this;
      }
      
      // optional int64 mileage = 4;
      private long mileage_ ;
      public boolean hasMileage() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      public long getMileage() {
        return mileage_;
      }
      public Builder setMileage(long value) {
        bitField0_ |= 0x00000020;
        mileage_ = value;
        onChanged();
        return this;
      }
      public Builder clearMileage() {
        bitField0_ = (bitField0_ & ~0x00000020);
        mileage_ = 0L;
        onChanged();
        return this;
      }
      
      // optional int32 staticDate = 5;
      private int staticDate_ ;
      public boolean hasStaticDate() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      public int getStaticDate() {
        return staticDate_;
      }
      public Builder setStaticDate(int value) {
        bitField0_ |= 0x00000040;
        staticDate_ = value;
        onChanged();
        return this;
      }
      public Builder clearStaticDate() {
        bitField0_ = (bitField0_ & ~0x00000040);
        staticDate_ = 0;
        onChanged();
        return this;
      }
      
      // optional int64 startDate = 6;
      private long startDate_ ;
      public boolean hasStartDate() {
        return ((bitField0_ & 0x00000080) == 0x00000080);
      }
      public long getStartDate() {
        return startDate_;
      }
      public Builder setStartDate(long value) {
        bitField0_ |= 0x00000080;
        startDate_ = value;
        onChanged();
        return this;
      }
      public Builder clearStartDate() {
        bitField0_ = (bitField0_ & ~0x00000080);
        startDate_ = 0L;
        onChanged();
        return this;
      }
      
      // optional int64 endDate = 7;
      private long endDate_ ;
      public boolean hasEndDate() {
        return ((bitField0_ & 0x00000100) == 0x00000100);
      }
      public long getEndDate() {
        return endDate_;
      }
      public Builder setEndDate(long value) {
        bitField0_ |= 0x00000100;
        endDate_ = value;
        onChanged();
        return this;
      }
      public Builder clearEndDate() {
        bitField0_ = (bitField0_ & ~0x00000100);
        endDate_ = 0L;
        onChanged();
        return this;
      }
      
      // optional int32 beginLat = 8;
      private int beginLat_ ;
      public boolean hasBeginLat() {
        return ((bitField0_ & 0x00000200) == 0x00000200);
      }
      public int getBeginLat() {
        return beginLat_;
      }
      public Builder setBeginLat(int value) {
        bitField0_ |= 0x00000200;
        beginLat_ = value;
        onChanged();
        return this;
      }
      public Builder clearBeginLat() {
        bitField0_ = (bitField0_ & ~0x00000200);
        beginLat_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 beginLng = 9;
      private int beginLng_ ;
      public boolean hasBeginLng() {
        return ((bitField0_ & 0x00000400) == 0x00000400);
      }
      public int getBeginLng() {
        return beginLng_;
      }
      public Builder setBeginLng(int value) {
        bitField0_ |= 0x00000400;
        beginLng_ = value;
        onChanged();
        return this;
      }
      public Builder clearBeginLng() {
        bitField0_ = (bitField0_ & ~0x00000400);
        beginLng_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 endLat = 10;
      private int endLat_ ;
      public boolean hasEndLat() {
        return ((bitField0_ & 0x00000800) == 0x00000800);
      }
      public int getEndLat() {
        return endLat_;
      }
      public Builder setEndLat(int value) {
        bitField0_ |= 0x00000800;
        endLat_ = value;
        onChanged();
        return this;
      }
      public Builder clearEndLat() {
        bitField0_ = (bitField0_ & ~0x00000800);
        endLat_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 endLng = 11;
      private int endLng_ ;
      public boolean hasEndLng() {
        return ((bitField0_ & 0x00001000) == 0x00001000);
      }
      public int getEndLng() {
        return endLng_;
      }
      public Builder setEndLng(int value) {
        bitField0_ |= 0x00001000;
        endLng_ = value;
        onChanged();
        return this;
      }
      public Builder clearEndLng() {
        bitField0_ = (bitField0_ & ~0x00001000);
        endLng_ = 0;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:MileageSummary)
    }
    
    static {
      defaultInstance = new MileageSummary(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:MileageSummary)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_MileageSummary_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MileageSummary_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n@core/proto/webservice/statisticsdata/e" +
      "ntity/MileageSummary.proto\"\207\002\n\016MileageSu" +
      "mmary\022\022\n\nterminalID\030\001 \002(\003\022\024\n\014recordsTota" +
      "l\030\014 \001(\005\022\024\n\014calculatedAT\030\r \001(\005\022\024\n\014beginMi" +
      "leage\030\016 \001(\003\022\022\n\nendMileage\030\003 \001(\003\022\017\n\007milea" +
      "ge\030\004 \001(\003\022\022\n\nstaticDate\030\005 \001(\005\022\021\n\tstartDat" +
      "e\030\006 \001(\003\022\017\n\007endDate\030\007 \001(\003\022\020\n\010beginLat\030\010 \001" +
      "(\005\022\020\n\010beginLng\030\t \001(\005\022\016\n\006endLat\030\n \001(\005\022\016\n\006" +
      "endLng\030\013 \001(\005Bc\nOcom.navinfo.opentsp.plat" +
      "form.location.protocol.webservice.statis",
      "ticsdata.entityB\020LCMileageSummary"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_MileageSummary_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_MileageSummary_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_MileageSummary_descriptor,
              new String[] { "TerminalID", "RecordsTotal", "CalculatedAT", "BeginMileage", "EndMileage", "Mileage", "StaticDate", "StartDate", "EndDate", "BeginLat", "BeginLng", "EndLat", "EndLng", },
              MileageSummary.class,
              MileageSummary.Builder.class);
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
