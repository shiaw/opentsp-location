// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/common/java/VehicleStatus.proto

package com.navinfo.opentsp.platform.location.protocol.common;

public final class LCVehicleStatus {
  private LCVehicleStatus() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface VehicleStatusOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
  }
  public static final class VehicleStatus extends
      com.google.protobuf.GeneratedMessage
      implements VehicleStatusOrBuilder {
    // Use VehicleStatus.newBuilder() to construct.
    private VehicleStatus(Builder builder) {
      super(builder);
    }
    private VehicleStatus(boolean noInit) {}
    
    private static final VehicleStatus defaultInstance;
    public static VehicleStatus getDefaultInstance() {
      return defaultInstance;
    }
    
    public VehicleStatus getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return LCVehicleStatus.internal_static_VehicleStatus_descriptor;
    }
    
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return LCVehicleStatus.internal_static_VehicleStatus_fieldAccessorTable;
    }
    
    public enum Status
        implements com.google.protobuf.ProtocolMessageEnum {
      acc(0, 1),
      isLocation(1, 2),
      northSouthLat(2, 4),
      eastWestLng(3, 8),
      operateStatus(4, 16),
      isEncrypted(5, 32),
      emptyLoad(6, 0),
      halfLoad(7, 256),
      fullLoad(8, 768),
      oilNormal(9, 1024),
      powerNormal(10, 2048),
      isLocked(11, 4096),
      frontDoorSwitch(12, 8192),
      middleDoorSwitch(13, 16384),
      backDoorSwitch(14, 32768),
      driverDoorSwitch(15, 65536),
      defineByYourself(16, 131072),
      isUsingGps(17, 262144),
      isUsingBeidou(18, 524288),
      isUsingGlonass(19, 1048576),
      isUsingGalileo(20, 2097152),
      ;
      
      public static final int acc_VALUE = 1;
      public static final int isLocation_VALUE = 2;
      public static final int northSouthLat_VALUE = 4;
      public static final int eastWestLng_VALUE = 8;
      public static final int operateStatus_VALUE = 16;
      public static final int isEncrypted_VALUE = 32;
      public static final int emptyLoad_VALUE = 0;
      public static final int halfLoad_VALUE = 256;
      public static final int fullLoad_VALUE = 768;
      public static final int oilNormal_VALUE = 1024;
      public static final int powerNormal_VALUE = 2048;
      public static final int isLocked_VALUE = 4096;
      public static final int frontDoorSwitch_VALUE = 8192;
      public static final int middleDoorSwitch_VALUE = 16384;
      public static final int backDoorSwitch_VALUE = 32768;
      public static final int driverDoorSwitch_VALUE = 65536;
      public static final int defineByYourself_VALUE = 131072;
      public static final int isUsingGps_VALUE = 262144;
      public static final int isUsingBeidou_VALUE = 524288;
      public static final int isUsingGlonass_VALUE = 1048576;
      public static final int isUsingGalileo_VALUE = 2097152;
      
      
      public final int getNumber() { return value; }
      
      public static Status valueOf(int value) {
        switch (value) {
          case 1: return acc;
          case 2: return isLocation;
          case 4: return northSouthLat;
          case 8: return eastWestLng;
          case 16: return operateStatus;
          case 32: return isEncrypted;
          case 0: return emptyLoad;
          case 256: return halfLoad;
          case 768: return fullLoad;
          case 1024: return oilNormal;
          case 2048: return powerNormal;
          case 4096: return isLocked;
          case 8192: return frontDoorSwitch;
          case 16384: return middleDoorSwitch;
          case 32768: return backDoorSwitch;
          case 65536: return driverDoorSwitch;
          case 131072: return defineByYourself;
          case 262144: return isUsingGps;
          case 524288: return isUsingBeidou;
          case 1048576: return isUsingGlonass;
          case 2097152: return isUsingGalileo;
          default: return null;
        }
      }
      
      public static com.google.protobuf.Internal.EnumLiteMap<Status>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static com.google.protobuf.Internal.EnumLiteMap<Status>
          internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<Status>() {
              public Status findValueByNumber(int number) {
                return Status.valueOf(number);
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
        return VehicleStatus.getDescriptor().getEnumTypes().get(0);
      }
      
      private static final Status[] VALUES = {
        acc, isLocation, northSouthLat, eastWestLng, operateStatus, isEncrypted, emptyLoad, halfLoad, fullLoad, oilNormal, powerNormal, isLocked, frontDoorSwitch, middleDoorSwitch, backDoorSwitch, driverDoorSwitch, defineByYourself, isUsingGps, isUsingBeidou, isUsingGlonass, isUsingGalileo, 
      };
      
      public static Status valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        return VALUES[desc.getIndex()];
      }
      
      private final int index;
      private final int value;
      
      private Status(int index, int value) {
        this.index = index;
        this.value = value;
      }
      
      // @@protoc_insertion_point(enum_scope:VehicleStatus.Status)
    }
    
    public enum StatusAddition
        implements com.google.protobuf.ProtocolMessageEnum {
      switchVoltage(0, 256),
      switchDoor(1, 512),
      ;
      
      public static final int switchVoltage_VALUE = 256;
      public static final int switchDoor_VALUE = 512;
      
      
      public final int getNumber() { return value; }
      
      public static StatusAddition valueOf(int value) {
        switch (value) {
          case 256: return switchVoltage;
          case 512: return switchDoor;
          default: return null;
        }
      }
      
      public static com.google.protobuf.Internal.EnumLiteMap<StatusAddition>
          internalGetValueMap() {
        return internalValueMap;
      }
      private static com.google.protobuf.Internal.EnumLiteMap<StatusAddition>
          internalValueMap =
            new com.google.protobuf.Internal.EnumLiteMap<StatusAddition>() {
              public StatusAddition findValueByNumber(int number) {
                return StatusAddition.valueOf(number);
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
        return VehicleStatus.getDescriptor().getEnumTypes().get(1);
      }
      
      private static final StatusAddition[] VALUES = {
        switchVoltage, switchDoor, 
      };
      
      public static StatusAddition valueOf(
          com.google.protobuf.Descriptors.EnumValueDescriptor desc) {
        if (desc.getType() != getDescriptor()) {
          throw new IllegalArgumentException(
            "EnumValueDescriptor is not for this type.");
        }
        return VALUES[desc.getIndex()];
      }
      
      private final int index;
      private final int value;
      
      private StatusAddition(int index, int value) {
        this.index = index;
        this.value = value;
      }
      
      // @@protoc_insertion_point(enum_scope:VehicleStatus.StatusAddition)
    }
    
    private void initFields() {
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
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
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
    
    public static VehicleStatus parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static VehicleStatus parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static VehicleStatus parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static VehicleStatus parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static VehicleStatus parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static VehicleStatus parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static VehicleStatus parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static VehicleStatus parseDelimitedFrom(
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
    public static VehicleStatus parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static VehicleStatus parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(VehicleStatus prototype) {
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
       implements VehicleStatusOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return LCVehicleStatus.internal_static_VehicleStatus_descriptor;
      }
      
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return LCVehicleStatus.internal_static_VehicleStatus_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatus.VehicleStatus.newBuilder()
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
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return VehicleStatus.getDescriptor();
      }
      
      public VehicleStatus getDefaultInstanceForType() {
        return VehicleStatus.getDefaultInstance();
      }
      
      public VehicleStatus build() {
        VehicleStatus result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private VehicleStatus buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        VehicleStatus result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public VehicleStatus buildPartial() {
        VehicleStatus result = new VehicleStatus(this);
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof VehicleStatus) {
          return mergeFrom((VehicleStatus)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(VehicleStatus other) {
        if (other == VehicleStatus.getDefaultInstance()) return this;
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
          }
        }
      }
      
      
      // @@protoc_insertion_point(builder_scope:VehicleStatus)
    }
    
    static {
      defaultInstance = new VehicleStatus(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:VehicleStatus)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_VehicleStatus_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_VehicleStatus_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n*core/proto/common/java/VehicleStatus.p" +
      "roto\"\330\003\n\rVehicleStatus\"\217\003\n\006Status\022\007\n\003acc" +
      "\020\001\022\016\n\nisLocation\020\002\022\021\n\rnorthSouthLat\020\004\022\017\n" +
      "\013eastWestLng\020\010\022\021\n\roperateStatus\020\020\022\017\n\013isE" +
      "ncrypted\020 \022\r\n\temptyLoad\020\000\022\r\n\010halfLoad\020\200\002" +
      "\022\r\n\010fullLoad\020\200\006\022\016\n\toilNormal\020\200\010\022\020\n\013power" +
      "Normal\020\200\020\022\r\n\010isLocked\020\200 \022\024\n\017frontDoorSwi" +
      "tch\020\200@\022\026\n\020middleDoorSwitch\020\200\200\001\022\024\n\016backDo" +
      "orSwitch\020\200\200\002\022\026\n\020driverDoorSwitch\020\200\200\004\022\026\n\020" +
      "defineByYourself\020\200\200\010\022\020\n\nisUsingGps\020\200\200\020\022\023",
      "\n\risUsingBeidou\020\200\200 \022\024\n\016isUsingGlonass\020\200\200" +
      "@\022\025\n\016isUsingGalileo\020\200\200\200\001\"5\n\016StatusAdditi" +
      "on\022\022\n\rswitchVoltage\020\200\002\022\017\n\nswitchDoor\020\200\004B" +
      "H\n5com.navinfo.opentsp.platform.location" +
      ".protocol.commonB\017LCVehicleStatus"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_VehicleStatus_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_VehicleStatus_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_VehicleStatus_descriptor,
              new String[] { },
              VehicleStatus.class,
              VehicleStatus.Builder.class);
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
