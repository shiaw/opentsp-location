// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/common/java/TireTemperatureAddition.proto

package com.navinfo.opentsp.platform.location.protocol.common;

public final class LCTireTemperatureAddition {
  private LCTireTemperatureAddition() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface TireTemperatureAdditionOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // optional int32 vehType = 1;
    boolean hasVehType();
    int getVehType();
    
    // optional int32 tyrePosition = 2;
    boolean hasTyrePosition();
    int getTyrePosition();
    
    // optional int32 tyreCondition = 3;
    boolean hasTyreCondition();
    int getTyreCondition();
    
    // optional int32 tyrePressure = 4;
    boolean hasTyrePressure();
    int getTyrePressure();
    
    // optional int32 tyreTemperature = 5;
    boolean hasTyreTemperature();
    int getTyreTemperature();
    
    // optional int32 temAlarmThreshold = 6;
    boolean hasTemAlarmThreshold();
    int getTemAlarmThreshold();
    
    // optional int32 tyreHalarmThreshold = 7;
    boolean hasTyreHalarmThreshold();
    int getTyreHalarmThreshold();
    
    // optional int32 tyreLalarmThreshold = 8;
    boolean hasTyreLalarmThreshold();
    int getTyreLalarmThreshold();
    
    // optional int32 tyreNomimalValue = 9;
    boolean hasTyreNomimalValue();
    int getTyreNomimalValue();
  }
  public static final class TireTemperatureAddition extends
      com.google.protobuf.GeneratedMessage
      implements TireTemperatureAdditionOrBuilder {
    // Use TireTemperatureAddition.newBuilder() to construct.
    private TireTemperatureAddition(Builder builder) {
      super(builder);
    }
    private TireTemperatureAddition(boolean noInit) {}
    
    private static final TireTemperatureAddition defaultInstance;
    public static TireTemperatureAddition getDefaultInstance() {
      return defaultInstance;
    }
    
    public TireTemperatureAddition getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return LCTireTemperatureAddition.internal_static_TireTemperatureAddition_descriptor;
    }
    
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return LCTireTemperatureAddition.internal_static_TireTemperatureAddition_fieldAccessorTable;
    }
    
    private int bitField0_;
    // optional int32 vehType = 1;
    public static final int VEHTYPE_FIELD_NUMBER = 1;
    private int vehType_;
    public boolean hasVehType() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public int getVehType() {
      return vehType_;
    }
    
    // optional int32 tyrePosition = 2;
    public static final int TYREPOSITION_FIELD_NUMBER = 2;
    private int tyrePosition_;
    public boolean hasTyrePosition() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public int getTyrePosition() {
      return tyrePosition_;
    }
    
    // optional int32 tyreCondition = 3;
    public static final int TYRECONDITION_FIELD_NUMBER = 3;
    private int tyreCondition_;
    public boolean hasTyreCondition() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public int getTyreCondition() {
      return tyreCondition_;
    }
    
    // optional int32 tyrePressure = 4;
    public static final int TYREPRESSURE_FIELD_NUMBER = 4;
    private int tyrePressure_;
    public boolean hasTyrePressure() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public int getTyrePressure() {
      return tyrePressure_;
    }
    
    // optional int32 tyreTemperature = 5;
    public static final int TYRETEMPERATURE_FIELD_NUMBER = 5;
    private int tyreTemperature_;
    public boolean hasTyreTemperature() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public int getTyreTemperature() {
      return tyreTemperature_;
    }
    
    // optional int32 temAlarmThreshold = 6;
    public static final int TEMALARMTHRESHOLD_FIELD_NUMBER = 6;
    private int temAlarmThreshold_;
    public boolean hasTemAlarmThreshold() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    public int getTemAlarmThreshold() {
      return temAlarmThreshold_;
    }
    
    // optional int32 tyreHalarmThreshold = 7;
    public static final int TYREHALARMTHRESHOLD_FIELD_NUMBER = 7;
    private int tyreHalarmThreshold_;
    public boolean hasTyreHalarmThreshold() {
      return ((bitField0_ & 0x00000040) == 0x00000040);
    }
    public int getTyreHalarmThreshold() {
      return tyreHalarmThreshold_;
    }
    
    // optional int32 tyreLalarmThreshold = 8;
    public static final int TYRELALARMTHRESHOLD_FIELD_NUMBER = 8;
    private int tyreLalarmThreshold_;
    public boolean hasTyreLalarmThreshold() {
      return ((bitField0_ & 0x00000080) == 0x00000080);
    }
    public int getTyreLalarmThreshold() {
      return tyreLalarmThreshold_;
    }
    
    // optional int32 tyreNomimalValue = 9;
    public static final int TYRENOMIMALVALUE_FIELD_NUMBER = 9;
    private int tyreNomimalValue_;
    public boolean hasTyreNomimalValue() {
      return ((bitField0_ & 0x00000100) == 0x00000100);
    }
    public int getTyreNomimalValue() {
      return tyreNomimalValue_;
    }
    
    private void initFields() {
      vehType_ = 0;
      tyrePosition_ = 0;
      tyreCondition_ = 0;
      tyrePressure_ = 0;
      tyreTemperature_ = 0;
      temAlarmThreshold_ = 0;
      tyreHalarmThreshold_ = 0;
      tyreLalarmThreshold_ = 0;
      tyreNomimalValue_ = 0;
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
        output.writeInt32(1, vehType_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, tyrePosition_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeInt32(3, tyreCondition_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeInt32(4, tyrePressure_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeInt32(5, tyreTemperature_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeInt32(6, temAlarmThreshold_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        output.writeInt32(7, tyreHalarmThreshold_);
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        output.writeInt32(8, tyreLalarmThreshold_);
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        output.writeInt32(9, tyreNomimalValue_);
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
          .computeInt32Size(1, vehType_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, tyrePosition_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(3, tyreCondition_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(4, tyrePressure_);
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(5, tyreTemperature_);
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(6, temAlarmThreshold_);
      }
      if (((bitField0_ & 0x00000040) == 0x00000040)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(7, tyreHalarmThreshold_);
      }
      if (((bitField0_ & 0x00000080) == 0x00000080)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(8, tyreLalarmThreshold_);
      }
      if (((bitField0_ & 0x00000100) == 0x00000100)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(9, tyreNomimalValue_);
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
    
    public static TireTemperatureAddition parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static TireTemperatureAddition parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static TireTemperatureAddition parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static TireTemperatureAddition parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static TireTemperatureAddition parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static TireTemperatureAddition parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static TireTemperatureAddition parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static TireTemperatureAddition parseDelimitedFrom(
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
    public static TireTemperatureAddition parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static TireTemperatureAddition parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(TireTemperatureAddition prototype) {
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
       implements TireTemperatureAdditionOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return LCTireTemperatureAddition.internal_static_TireTemperatureAddition_descriptor;
      }
      
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return LCTireTemperatureAddition.internal_static_TireTemperatureAddition_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.common.LCTireTemperatureAddition.TireTemperatureAddition.newBuilder()
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
        vehType_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        tyrePosition_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        tyreCondition_ = 0;
        bitField0_ = (bitField0_ & ~0x00000004);
        tyrePressure_ = 0;
        bitField0_ = (bitField0_ & ~0x00000008);
        tyreTemperature_ = 0;
        bitField0_ = (bitField0_ & ~0x00000010);
        temAlarmThreshold_ = 0;
        bitField0_ = (bitField0_ & ~0x00000020);
        tyreHalarmThreshold_ = 0;
        bitField0_ = (bitField0_ & ~0x00000040);
        tyreLalarmThreshold_ = 0;
        bitField0_ = (bitField0_ & ~0x00000080);
        tyreNomimalValue_ = 0;
        bitField0_ = (bitField0_ & ~0x00000100);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return TireTemperatureAddition.getDescriptor();
      }
      
      public TireTemperatureAddition getDefaultInstanceForType() {
        return TireTemperatureAddition.getDefaultInstance();
      }
      
      public TireTemperatureAddition build() {
        TireTemperatureAddition result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private TireTemperatureAddition buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        TireTemperatureAddition result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public TireTemperatureAddition buildPartial() {
        TireTemperatureAddition result = new TireTemperatureAddition(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.vehType_ = vehType_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.tyrePosition_ = tyrePosition_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.tyreCondition_ = tyreCondition_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.tyrePressure_ = tyrePressure_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.tyreTemperature_ = tyreTemperature_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.temAlarmThreshold_ = temAlarmThreshold_;
        if (((from_bitField0_ & 0x00000040) == 0x00000040)) {
          to_bitField0_ |= 0x00000040;
        }
        result.tyreHalarmThreshold_ = tyreHalarmThreshold_;
        if (((from_bitField0_ & 0x00000080) == 0x00000080)) {
          to_bitField0_ |= 0x00000080;
        }
        result.tyreLalarmThreshold_ = tyreLalarmThreshold_;
        if (((from_bitField0_ & 0x00000100) == 0x00000100)) {
          to_bitField0_ |= 0x00000100;
        }
        result.tyreNomimalValue_ = tyreNomimalValue_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof TireTemperatureAddition) {
          return mergeFrom((TireTemperatureAddition)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(TireTemperatureAddition other) {
        if (other == TireTemperatureAddition.getDefaultInstance()) return this;
        if (other.hasVehType()) {
          setVehType(other.getVehType());
        }
        if (other.hasTyrePosition()) {
          setTyrePosition(other.getTyrePosition());
        }
        if (other.hasTyreCondition()) {
          setTyreCondition(other.getTyreCondition());
        }
        if (other.hasTyrePressure()) {
          setTyrePressure(other.getTyrePressure());
        }
        if (other.hasTyreTemperature()) {
          setTyreTemperature(other.getTyreTemperature());
        }
        if (other.hasTemAlarmThreshold()) {
          setTemAlarmThreshold(other.getTemAlarmThreshold());
        }
        if (other.hasTyreHalarmThreshold()) {
          setTyreHalarmThreshold(other.getTyreHalarmThreshold());
        }
        if (other.hasTyreLalarmThreshold()) {
          setTyreLalarmThreshold(other.getTyreLalarmThreshold());
        }
        if (other.hasTyreNomimalValue()) {
          setTyreNomimalValue(other.getTyreNomimalValue());
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
              vehType_ = input.readInt32();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              tyrePosition_ = input.readInt32();
              break;
            }
            case 24: {
              bitField0_ |= 0x00000004;
              tyreCondition_ = input.readInt32();
              break;
            }
            case 32: {
              bitField0_ |= 0x00000008;
              tyrePressure_ = input.readInt32();
              break;
            }
            case 40: {
              bitField0_ |= 0x00000010;
              tyreTemperature_ = input.readInt32();
              break;
            }
            case 48: {
              bitField0_ |= 0x00000020;
              temAlarmThreshold_ = input.readInt32();
              break;
            }
            case 56: {
              bitField0_ |= 0x00000040;
              tyreHalarmThreshold_ = input.readInt32();
              break;
            }
            case 64: {
              bitField0_ |= 0x00000080;
              tyreLalarmThreshold_ = input.readInt32();
              break;
            }
            case 72: {
              bitField0_ |= 0x00000100;
              tyreNomimalValue_ = input.readInt32();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // optional int32 vehType = 1;
      private int vehType_ ;
      public boolean hasVehType() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public int getVehType() {
        return vehType_;
      }
      public Builder setVehType(int value) {
        bitField0_ |= 0x00000001;
        vehType_ = value;
        onChanged();
        return this;
      }
      public Builder clearVehType() {
        bitField0_ = (bitField0_ & ~0x00000001);
        vehType_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 tyrePosition = 2;
      private int tyrePosition_ ;
      public boolean hasTyrePosition() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public int getTyrePosition() {
        return tyrePosition_;
      }
      public Builder setTyrePosition(int value) {
        bitField0_ |= 0x00000002;
        tyrePosition_ = value;
        onChanged();
        return this;
      }
      public Builder clearTyrePosition() {
        bitField0_ = (bitField0_ & ~0x00000002);
        tyrePosition_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 tyreCondition = 3;
      private int tyreCondition_ ;
      public boolean hasTyreCondition() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public int getTyreCondition() {
        return tyreCondition_;
      }
      public Builder setTyreCondition(int value) {
        bitField0_ |= 0x00000004;
        tyreCondition_ = value;
        onChanged();
        return this;
      }
      public Builder clearTyreCondition() {
        bitField0_ = (bitField0_ & ~0x00000004);
        tyreCondition_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 tyrePressure = 4;
      private int tyrePressure_ ;
      public boolean hasTyrePressure() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public int getTyrePressure() {
        return tyrePressure_;
      }
      public Builder setTyrePressure(int value) {
        bitField0_ |= 0x00000008;
        tyrePressure_ = value;
        onChanged();
        return this;
      }
      public Builder clearTyrePressure() {
        bitField0_ = (bitField0_ & ~0x00000008);
        tyrePressure_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 tyreTemperature = 5;
      private int tyreTemperature_ ;
      public boolean hasTyreTemperature() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public int getTyreTemperature() {
        return tyreTemperature_;
      }
      public Builder setTyreTemperature(int value) {
        bitField0_ |= 0x00000010;
        tyreTemperature_ = value;
        onChanged();
        return this;
      }
      public Builder clearTyreTemperature() {
        bitField0_ = (bitField0_ & ~0x00000010);
        tyreTemperature_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 temAlarmThreshold = 6;
      private int temAlarmThreshold_ ;
      public boolean hasTemAlarmThreshold() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      public int getTemAlarmThreshold() {
        return temAlarmThreshold_;
      }
      public Builder setTemAlarmThreshold(int value) {
        bitField0_ |= 0x00000020;
        temAlarmThreshold_ = value;
        onChanged();
        return this;
      }
      public Builder clearTemAlarmThreshold() {
        bitField0_ = (bitField0_ & ~0x00000020);
        temAlarmThreshold_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 tyreHalarmThreshold = 7;
      private int tyreHalarmThreshold_ ;
      public boolean hasTyreHalarmThreshold() {
        return ((bitField0_ & 0x00000040) == 0x00000040);
      }
      public int getTyreHalarmThreshold() {
        return tyreHalarmThreshold_;
      }
      public Builder setTyreHalarmThreshold(int value) {
        bitField0_ |= 0x00000040;
        tyreHalarmThreshold_ = value;
        onChanged();
        return this;
      }
      public Builder clearTyreHalarmThreshold() {
        bitField0_ = (bitField0_ & ~0x00000040);
        tyreHalarmThreshold_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 tyreLalarmThreshold = 8;
      private int tyreLalarmThreshold_ ;
      public boolean hasTyreLalarmThreshold() {
        return ((bitField0_ & 0x00000080) == 0x00000080);
      }
      public int getTyreLalarmThreshold() {
        return tyreLalarmThreshold_;
      }
      public Builder setTyreLalarmThreshold(int value) {
        bitField0_ |= 0x00000080;
        tyreLalarmThreshold_ = value;
        onChanged();
        return this;
      }
      public Builder clearTyreLalarmThreshold() {
        bitField0_ = (bitField0_ & ~0x00000080);
        tyreLalarmThreshold_ = 0;
        onChanged();
        return this;
      }
      
      // optional int32 tyreNomimalValue = 9;
      private int tyreNomimalValue_ ;
      public boolean hasTyreNomimalValue() {
        return ((bitField0_ & 0x00000100) == 0x00000100);
      }
      public int getTyreNomimalValue() {
        return tyreNomimalValue_;
      }
      public Builder setTyreNomimalValue(int value) {
        bitField0_ |= 0x00000100;
        tyreNomimalValue_ = value;
        onChanged();
        return this;
      }
      public Builder clearTyreNomimalValue() {
        bitField0_ = (bitField0_ & ~0x00000100);
        tyreNomimalValue_ = 0;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:TireTemperatureAddition)
    }
    
    static {
      defaultInstance = new TireTemperatureAddition(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:TireTemperatureAddition)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_TireTemperatureAddition_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_TireTemperatureAddition_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n4core/proto/common/java/TireTemperature" +
      "Addition.proto\"\365\001\n\027TireTemperatureAdditi" +
      "on\022\017\n\007vehType\030\001 \001(\005\022\024\n\014tyrePosition\030\002 \001(" +
      "\005\022\025\n\rtyreCondition\030\003 \001(\005\022\024\n\014tyrePressure" +
      "\030\004 \001(\005\022\027\n\017tyreTemperature\030\005 \001(\005\022\031\n\021temAl" +
      "armThreshold\030\006 \001(\005\022\033\n\023tyreHalarmThreshol" +
      "d\030\007 \001(\005\022\033\n\023tyreLalarmThreshold\030\010 \001(\005\022\030\n\020" +
      "tyreNomimalValue\030\t \001(\005BR\n5com.navinfo.op" +
      "entsp.platform.location.protocol.commonB" +
      "\031LCTireTemperatureAddition"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_TireTemperatureAddition_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_TireTemperatureAddition_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_TireTemperatureAddition_descriptor,
              new String[] { "VehType", "TyrePosition", "TyreCondition", "TyrePressure", "TyreTemperature", "TemAlarmThreshold", "TyreHalarmThreshold", "TyreLalarmThreshold", "TyreNomimalValue", },
              TireTemperatureAddition.class,
              TireTemperatureAddition.Builder.class);
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