// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/terminal/CANBUSData/Report/MessageOfMFD1.proto

package com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.Report;

public final class LCMessageOfMFD1 {
  private LCMessageOfMFD1() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public enum OilPressureLow
      implements com.google.protobuf.ProtocolMessageEnum {
    oilPressureNormal(0, 0),
    belowOperateRange(1, 1),
    notAvailablePressure(2, 3),
    ;
    
    public static final int oilPressureNormal_VALUE = 0;
    public static final int belowOperateRange_VALUE = 1;
    public static final int notAvailablePressure_VALUE = 3;
    
    
    public final int getNumber() { return value; }
    
    public static OilPressureLow valueOf(int value) {
      switch (value) {
        case 0: return oilPressureNormal;
        case 1: return belowOperateRange;
        case 3: return notAvailablePressure;
        default: return null;
      }
    }
    
    public static com.google.protobuf.Internal.EnumLiteMap<OilPressureLow>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<OilPressureLow>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<OilPressureLow>() {
            public OilPressureLow findValueByNumber(int number) {
              return OilPressureLow.valueOf(number);
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
      return LCMessageOfMFD1.getDescriptor().getEnumTypes().get(0);
    }
    
    private static final OilPressureLow[] VALUES = {
      oilPressureNormal, belowOperateRange, notAvailablePressure, 
    };
    
    public static OilPressureLow valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }
    
    private final int index;
    private final int value;
    
    private OilPressureLow(int index, int value) {
      this.index = index;
      this.value = value;
    }
    
    // @@protoc_insertion_point(enum_scope:OilPressureLow)
  }
  
  public enum OverCoolantTem
      implements com.google.protobuf.ProtocolMessageEnum {
    coolantNormal(0, 0),
    coolantPreWarning(1, 1),
    coolantWarning(2, 3),
    ;
    
    public static final int coolantNormal_VALUE = 0;
    public static final int coolantPreWarning_VALUE = 1;
    public static final int coolantWarning_VALUE = 3;
    
    
    public final int getNumber() { return value; }
    
    public static OverCoolantTem valueOf(int value) {
      switch (value) {
        case 0: return coolantNormal;
        case 1: return coolantPreWarning;
        case 3: return coolantWarning;
        default: return null;
      }
    }
    
    public static com.google.protobuf.Internal.EnumLiteMap<OverCoolantTem>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<OverCoolantTem>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<OverCoolantTem>() {
            public OverCoolantTem findValueByNumber(int number) {
              return OverCoolantTem.valueOf(number);
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
      return LCMessageOfMFD1.getDescriptor().getEnumTypes().get(1);
    }
    
    private static final OverCoolantTem[] VALUES = {
      coolantNormal, coolantPreWarning, coolantWarning, 
    };
    
    public static OverCoolantTem valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }
    
    private final int index;
    private final int value;
    
    private OverCoolantTem(int index, int value) {
      this.index = index;
      this.value = value;
    }
    
    // @@protoc_insertion_point(enum_scope:OverCoolantTem)
  }
  
  public enum ColdStartHeaterStatus
      implements com.google.protobuf.ProtocolMessageEnum {
    offPhase(0, 0),
    preHeatingPhase(1, 1),
    standbyPhaseWithHeat(2, 2),
    standbyPhaseWithoutHeat(3, 3),
    crankWithExtraHeatPhase(4, 4),
    crankPhase(5, 5),
    postHeatPhase(6, 6),
    heatingPhaseEnd(7, 7),
    afterRunPhase(8, 8),
    ;
    
    public static final int offPhase_VALUE = 0;
    public static final int preHeatingPhase_VALUE = 1;
    public static final int standbyPhaseWithHeat_VALUE = 2;
    public static final int standbyPhaseWithoutHeat_VALUE = 3;
    public static final int crankWithExtraHeatPhase_VALUE = 4;
    public static final int crankPhase_VALUE = 5;
    public static final int postHeatPhase_VALUE = 6;
    public static final int heatingPhaseEnd_VALUE = 7;
    public static final int afterRunPhase_VALUE = 8;
    
    
    public final int getNumber() { return value; }
    
    public static ColdStartHeaterStatus valueOf(int value) {
      switch (value) {
        case 0: return offPhase;
        case 1: return preHeatingPhase;
        case 2: return standbyPhaseWithHeat;
        case 3: return standbyPhaseWithoutHeat;
        case 4: return crankWithExtraHeatPhase;
        case 5: return crankPhase;
        case 6: return postHeatPhase;
        case 7: return heatingPhaseEnd;
        case 8: return afterRunPhase;
        default: return null;
      }
    }
    
    public static com.google.protobuf.Internal.EnumLiteMap<ColdStartHeaterStatus>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<ColdStartHeaterStatus>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<ColdStartHeaterStatus>() {
            public ColdStartHeaterStatus findValueByNumber(int number) {
              return ColdStartHeaterStatus.valueOf(number);
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
      return LCMessageOfMFD1.getDescriptor().getEnumTypes().get(2);
    }
    
    private static final ColdStartHeaterStatus[] VALUES = {
      offPhase, preHeatingPhase, standbyPhaseWithHeat, standbyPhaseWithoutHeat, crankWithExtraHeatPhase, crankPhase, postHeatPhase, heatingPhaseEnd, afterRunPhase, 
    };
    
    public static ColdStartHeaterStatus valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }
    
    private final int index;
    private final int value;
    
    private ColdStartHeaterStatus(int index, int value) {
      this.index = index;
      this.value = value;
    }
    
    // @@protoc_insertion_point(enum_scope:ColdStartHeaterStatus)
  }
  
  public enum OBDLampStatus
      implements com.google.protobuf.ProtocolMessageEnum {
    obdNormal(0, 0),
    obdPreWarning(1, 1),
    obdWarning(2, 3),
    ;
    
    public static final int obdNormal_VALUE = 0;
    public static final int obdPreWarning_VALUE = 1;
    public static final int obdWarning_VALUE = 3;
    
    
    public final int getNumber() { return value; }
    
    public static OBDLampStatus valueOf(int value) {
      switch (value) {
        case 0: return obdNormal;
        case 1: return obdPreWarning;
        case 3: return obdWarning;
        default: return null;
      }
    }
    
    public static com.google.protobuf.Internal.EnumLiteMap<OBDLampStatus>
        internalGetValueMap() {
      return internalValueMap;
    }
    private static com.google.protobuf.Internal.EnumLiteMap<OBDLampStatus>
        internalValueMap =
          new com.google.protobuf.Internal.EnumLiteMap<OBDLampStatus>() {
            public OBDLampStatus findValueByNumber(int number) {
              return OBDLampStatus.valueOf(number);
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
      return LCMessageOfMFD1.getDescriptor().getEnumTypes().get(3);
    }
    
    private static final OBDLampStatus[] VALUES = {
      obdNormal, obdPreWarning, obdWarning, 
    };
    
    public static OBDLampStatus valueOf(
        com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
      if (desc.getType() != getDescriptor()) {
        throw new IllegalArgumentException(
          "EnumValueDescriptor is not for this type.");
      }
      return VALUES[desc.getIndex()];
    }
    
    private final int index;
    private final int value;
    
    private OBDLampStatus(int index, int value) {
      this.index = index;
      this.value = value;
    }
    
    // @@protoc_insertion_point(enum_scope:OBDLampStatus)
  }
  
  public interface MessageOfMFD1OrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // optional int32 relativeOilPressure = 1;
    boolean hasRelativeOilPressure();
    int getRelativeOilPressure();
    
    // optional .OilPressureLow status = 2;
    boolean hasStatus();
    OilPressureLow getStatus();
    
    // optional .OverCoolantTem temperature = 3;
    boolean hasTemperature();
    OverCoolantTem getTemperature();
    
    // optional .ColdStartHeaterStatus coldStart = 4;
    boolean hasColdStart();
    ColdStartHeaterStatus getColdStart();
    
    // optional .OBDLampStatus obdLampStatus = 5;
    boolean hasObdLampStatus();
    OBDLampStatus getObdLampStatus();
    
    // optional float exhaustFlapValveOutput = 6;
    boolean hasExhaustFlapValveOutput();
    float getExhaustFlapValveOutput();
  }
  public static final class MessageOfMFD1 extends
      com.google.protobuf.GeneratedMessage
      implements MessageOfMFD1OrBuilder {
    // Use MessageOfMFD1.newBuilder() to construct.
    private MessageOfMFD1(Builder builder) {
      super(builder);
    }
    private MessageOfMFD1(boolean noInit) {}
    
    private static final MessageOfMFD1 defaultInstance;
    public static MessageOfMFD1 getDefaultInstance() {
      return defaultInstance;
    }
    
    public MessageOfMFD1 getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return LCMessageOfMFD1.internal_static_MessageOfMFD1_descriptor;
    }
    
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return LCMessageOfMFD1.internal_static_MessageOfMFD1_fieldAccessorTable;
    }
    
    private int bitField0_;
    // optional int32 relativeOilPressure = 1;
    public static final int RELATIVEOILPRESSURE_FIELD_NUMBER = 1;
    private int relativeOilPressure_;
    public boolean hasRelativeOilPressure() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public int getRelativeOilPressure() {
      return relativeOilPressure_;
    }
    
    // optional .OilPressureLow status = 2;
    public static final int STATUS_FIELD_NUMBER = 2;
    private OilPressureLow status_;
    public boolean hasStatus() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public OilPressureLow getStatus() {
      return status_;
    }
    
    // optional .OverCoolantTem temperature = 3;
    public static final int TEMPERATURE_FIELD_NUMBER = 3;
    private OverCoolantTem temperature_;
    public boolean hasTemperature() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public OverCoolantTem getTemperature() {
      return temperature_;
    }
    
    // optional .ColdStartHeaterStatus coldStart = 4;
    public static final int COLDSTART_FIELD_NUMBER = 4;
    private ColdStartHeaterStatus coldStart_;
    public boolean hasColdStart() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public ColdStartHeaterStatus getColdStart() {
      return coldStart_;
    }
    
    // optional .OBDLampStatus obdLampStatus = 5;
    public static final int OBDLAMPSTATUS_FIELD_NUMBER = 5;
    private OBDLampStatus obdLampStatus_;
    public boolean hasObdLampStatus() {
      return ((bitField0_ & 0x00000010) == 0x00000010);
    }
    public OBDLampStatus getObdLampStatus() {
      return obdLampStatus_;
    }
    
    // optional float exhaustFlapValveOutput = 6;
    public static final int EXHAUSTFLAPVALVEOUTPUT_FIELD_NUMBER = 6;
    private float exhaustFlapValveOutput_;
    public boolean hasExhaustFlapValveOutput() {
      return ((bitField0_ & 0x00000020) == 0x00000020);
    }
    public float getExhaustFlapValveOutput() {
      return exhaustFlapValveOutput_;
    }
    
    private void initFields() {
      relativeOilPressure_ = 0;
      status_ = OilPressureLow.oilPressureNormal;
      temperature_ = OverCoolantTem.coolantNormal;
      coldStart_ = ColdStartHeaterStatus.offPhase;
      obdLampStatus_ = OBDLampStatus.obdNormal;
      exhaustFlapValveOutput_ = 0F;
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
        output.writeInt32(1, relativeOilPressure_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeEnum(2, status_.getNumber());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeEnum(3, temperature_.getNumber());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeEnum(4, coldStart_.getNumber());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        output.writeEnum(5, obdLampStatus_.getNumber());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        output.writeFloat(6, exhaustFlapValveOutput_);
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
          .computeInt32Size(1, relativeOilPressure_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(2, status_.getNumber());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(3, temperature_.getNumber());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(4, coldStart_.getNumber());
      }
      if (((bitField0_ & 0x00000010) == 0x00000010)) {
        size += com.google.protobuf.CodedOutputStream
          .computeEnumSize(5, obdLampStatus_.getNumber());
      }
      if (((bitField0_ & 0x00000020) == 0x00000020)) {
        size += com.google.protobuf.CodedOutputStream
          .computeFloatSize(6, exhaustFlapValveOutput_);
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
    
    public static MessageOfMFD1 parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static MessageOfMFD1 parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static MessageOfMFD1 parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static MessageOfMFD1 parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static MessageOfMFD1 parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static MessageOfMFD1 parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static MessageOfMFD1 parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static MessageOfMFD1 parseDelimitedFrom(
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
    public static MessageOfMFD1 parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static MessageOfMFD1 parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(MessageOfMFD1 prototype) {
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
       implements MessageOfMFD1OrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return LCMessageOfMFD1.internal_static_MessageOfMFD1_descriptor;
      }
      
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return LCMessageOfMFD1.internal_static_MessageOfMFD1_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.Report.LCMessageOfMFD1.MessageOfMFD1.newBuilder()
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
        relativeOilPressure_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        status_ = OilPressureLow.oilPressureNormal;
        bitField0_ = (bitField0_ & ~0x00000002);
        temperature_ = OverCoolantTem.coolantNormal;
        bitField0_ = (bitField0_ & ~0x00000004);
        coldStart_ = ColdStartHeaterStatus.offPhase;
        bitField0_ = (bitField0_ & ~0x00000008);
        obdLampStatus_ = OBDLampStatus.obdNormal;
        bitField0_ = (bitField0_ & ~0x00000010);
        exhaustFlapValveOutput_ = 0F;
        bitField0_ = (bitField0_ & ~0x00000020);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return MessageOfMFD1.getDescriptor();
      }
      
      public MessageOfMFD1 getDefaultInstanceForType() {
        return MessageOfMFD1.getDefaultInstance();
      }
      
      public MessageOfMFD1 build() {
        MessageOfMFD1 result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private MessageOfMFD1 buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        MessageOfMFD1 result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public MessageOfMFD1 buildPartial() {
        MessageOfMFD1 result = new MessageOfMFD1(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.relativeOilPressure_ = relativeOilPressure_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.status_ = status_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.temperature_ = temperature_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.coldStart_ = coldStart_;
        if (((from_bitField0_ & 0x00000010) == 0x00000010)) {
          to_bitField0_ |= 0x00000010;
        }
        result.obdLampStatus_ = obdLampStatus_;
        if (((from_bitField0_ & 0x00000020) == 0x00000020)) {
          to_bitField0_ |= 0x00000020;
        }
        result.exhaustFlapValveOutput_ = exhaustFlapValveOutput_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof MessageOfMFD1) {
          return mergeFrom((MessageOfMFD1)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(MessageOfMFD1 other) {
        if (other == MessageOfMFD1.getDefaultInstance()) return this;
        if (other.hasRelativeOilPressure()) {
          setRelativeOilPressure(other.getRelativeOilPressure());
        }
        if (other.hasStatus()) {
          setStatus(other.getStatus());
        }
        if (other.hasTemperature()) {
          setTemperature(other.getTemperature());
        }
        if (other.hasColdStart()) {
          setColdStart(other.getColdStart());
        }
        if (other.hasObdLampStatus()) {
          setObdLampStatus(other.getObdLampStatus());
        }
        if (other.hasExhaustFlapValveOutput()) {
          setExhaustFlapValveOutput(other.getExhaustFlapValveOutput());
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
              relativeOilPressure_ = input.readInt32();
              break;
            }
            case 16: {
              int rawValue = input.readEnum();
              OilPressureLow value = OilPressureLow.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(2, rawValue);
              } else {
                bitField0_ |= 0x00000002;
                status_ = value;
              }
              break;
            }
            case 24: {
              int rawValue = input.readEnum();
              OverCoolantTem value = OverCoolantTem.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(3, rawValue);
              } else {
                bitField0_ |= 0x00000004;
                temperature_ = value;
              }
              break;
            }
            case 32: {
              int rawValue = input.readEnum();
              ColdStartHeaterStatus value = ColdStartHeaterStatus.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(4, rawValue);
              } else {
                bitField0_ |= 0x00000008;
                coldStart_ = value;
              }
              break;
            }
            case 40: {
              int rawValue = input.readEnum();
              OBDLampStatus value = OBDLampStatus.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(5, rawValue);
              } else {
                bitField0_ |= 0x00000010;
                obdLampStatus_ = value;
              }
              break;
            }
            case 53: {
              bitField0_ |= 0x00000020;
              exhaustFlapValveOutput_ = input.readFloat();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // optional int32 relativeOilPressure = 1;
      private int relativeOilPressure_ ;
      public boolean hasRelativeOilPressure() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public int getRelativeOilPressure() {
        return relativeOilPressure_;
      }
      public Builder setRelativeOilPressure(int value) {
        bitField0_ |= 0x00000001;
        relativeOilPressure_ = value;
        onChanged();
        return this;
      }
      public Builder clearRelativeOilPressure() {
        bitField0_ = (bitField0_ & ~0x00000001);
        relativeOilPressure_ = 0;
        onChanged();
        return this;
      }
      
      // optional .OilPressureLow status = 2;
      private OilPressureLow status_ = OilPressureLow.oilPressureNormal;
      public boolean hasStatus() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public OilPressureLow getStatus() {
        return status_;
      }
      public Builder setStatus(OilPressureLow value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000002;
        status_ = value;
        onChanged();
        return this;
      }
      public Builder clearStatus() {
        bitField0_ = (bitField0_ & ~0x00000002);
        status_ = OilPressureLow.oilPressureNormal;
        onChanged();
        return this;
      }
      
      // optional .OverCoolantTem temperature = 3;
      private OverCoolantTem temperature_ = OverCoolantTem.coolantNormal;
      public boolean hasTemperature() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public OverCoolantTem getTemperature() {
        return temperature_;
      }
      public Builder setTemperature(OverCoolantTem value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000004;
        temperature_ = value;
        onChanged();
        return this;
      }
      public Builder clearTemperature() {
        bitField0_ = (bitField0_ & ~0x00000004);
        temperature_ = OverCoolantTem.coolantNormal;
        onChanged();
        return this;
      }
      
      // optional .ColdStartHeaterStatus coldStart = 4;
      private ColdStartHeaterStatus coldStart_ = ColdStartHeaterStatus.offPhase;
      public boolean hasColdStart() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public ColdStartHeaterStatus getColdStart() {
        return coldStart_;
      }
      public Builder setColdStart(ColdStartHeaterStatus value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000008;
        coldStart_ = value;
        onChanged();
        return this;
      }
      public Builder clearColdStart() {
        bitField0_ = (bitField0_ & ~0x00000008);
        coldStart_ = ColdStartHeaterStatus.offPhase;
        onChanged();
        return this;
      }
      
      // optional .OBDLampStatus obdLampStatus = 5;
      private OBDLampStatus obdLampStatus_ = OBDLampStatus.obdNormal;
      public boolean hasObdLampStatus() {
        return ((bitField0_ & 0x00000010) == 0x00000010);
      }
      public OBDLampStatus getObdLampStatus() {
        return obdLampStatus_;
      }
      public Builder setObdLampStatus(OBDLampStatus value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000010;
        obdLampStatus_ = value;
        onChanged();
        return this;
      }
      public Builder clearObdLampStatus() {
        bitField0_ = (bitField0_ & ~0x00000010);
        obdLampStatus_ = OBDLampStatus.obdNormal;
        onChanged();
        return this;
      }
      
      // optional float exhaustFlapValveOutput = 6;
      private float exhaustFlapValveOutput_ ;
      public boolean hasExhaustFlapValveOutput() {
        return ((bitField0_ & 0x00000020) == 0x00000020);
      }
      public float getExhaustFlapValveOutput() {
        return exhaustFlapValveOutput_;
      }
      public Builder setExhaustFlapValveOutput(float value) {
        bitField0_ |= 0x00000020;
        exhaustFlapValveOutput_ = value;
        onChanged();
        return this;
      }
      public Builder clearExhaustFlapValveOutput() {
        bitField0_ = (bitField0_ & ~0x00000020);
        exhaustFlapValveOutput_ = 0F;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:MessageOfMFD1)
    }
    
    static {
      defaultInstance = new MessageOfMFD1(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:MessageOfMFD1)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_MessageOfMFD1_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_MessageOfMFD1_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n9core/proto/terminal/CANBUSData/Report/" +
      "MessageOfMFD1.proto\"\345\001\n\rMessageOfMFD1\022\033\n" +
      "\023relativeOilPressure\030\001 \001(\005\022\037\n\006status\030\002 \001" +
      "(\0162\017.OilPressureLow\022$\n\013temperature\030\003 \001(\016" +
      "2\017.OverCoolantTem\022)\n\tcoldStart\030\004 \001(\0162\026.C" +
      "oldStartHeaterStatus\022%\n\robdLampStatus\030\005 " +
      "\001(\0162\016.OBDLampStatus\022\036\n\026exhaustFlapValveO" +
      "utput\030\006 \001(\002*X\n\016OilPressureLow\022\025\n\021oilPres" +
      "sureNormal\020\000\022\025\n\021belowOperateRange\020\001\022\030\n\024n" +
      "otAvailablePressure\020\003*N\n\016OverCoolantTem\022",
      "\021\n\rcoolantNormal\020\000\022\025\n\021coolantPreWarning\020" +
      "\001\022\022\n\016coolantWarning\020\003*\331\001\n\025ColdStartHeate" +
      "rStatus\022\014\n\010offPhase\020\000\022\023\n\017preHeatingPhase" +
      "\020\001\022\030\n\024standbyPhaseWithHeat\020\002\022\033\n\027standbyP" +
      "haseWithoutHeat\020\003\022\033\n\027crankWithExtraHeatP" +
      "hase\020\004\022\016\n\ncrankPhase\020\005\022\021\n\rpostHeatPhase\020" +
      "\006\022\023\n\017heatingPhaseEnd\020\007\022\021\n\rafterRunPhase\020" +
      "\010*A\n\rOBDLampStatus\022\r\n\tobdNormal\020\000\022\021\n\robd" +
      "PreWarning\020\001\022\016\n\nobdWarning\020\003B\\\nIcom.navi" +
      "nfo.opentsp.platform.location.protocol.t",
      "erminal.CANBUSData.ReportB\017LCMessageOfMF" +
      "D1"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_MessageOfMFD1_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_MessageOfMFD1_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_MessageOfMFD1_descriptor,
              new String[] { "RelativeOilPressure", "Status", "Temperature", "ColdStart", "ObdLampStatus", "ExhaustFlapValveOutput", },
              MessageOfMFD1.class,
              MessageOfMFD1.Builder.class);
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