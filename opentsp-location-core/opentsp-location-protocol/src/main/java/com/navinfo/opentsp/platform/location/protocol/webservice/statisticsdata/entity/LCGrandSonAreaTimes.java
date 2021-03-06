// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/webservice/statisticsdata/entity/GrandSonAreaTimes.proto

package com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity;

public final class LCGrandSonAreaTimes {
  private LCGrandSonAreaTimes() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface GrandSonAreaTimesOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int64 _id = 1;
    boolean hasId();
    long getId();
    
    // required int32 times = 2;
    boolean hasTimes();
    int getTimes();
  }
  public static final class GrandSonAreaTimes extends
      com.google.protobuf.GeneratedMessage
      implements GrandSonAreaTimesOrBuilder {
    // Use GrandSonAreaTimes.newBuilder() to construct.
    private GrandSonAreaTimes(Builder builder) {
      super(builder);
    }
    private GrandSonAreaTimes(boolean noInit) {}
    
    private static final GrandSonAreaTimes defaultInstance;
    public static GrandSonAreaTimes getDefaultInstance() {
      return defaultInstance;
    }
    
    public GrandSonAreaTimes getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return LCGrandSonAreaTimes.internal_static_GrandSonAreaTimes_descriptor;
    }
    
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return LCGrandSonAreaTimes.internal_static_GrandSonAreaTimes_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required int64 _id = 1;
    public static final int _ID_FIELD_NUMBER = 1;
    private long Id_;
    public boolean hasId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public long getId() {
      return Id_;
    }
    
    // required int32 times = 2;
    public static final int TIMES_FIELD_NUMBER = 2;
    private int times_;
    public boolean hasTimes() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public int getTimes() {
      return times_;
    }
    
    private void initFields() {
      Id_ = 0L;
      times_ = 0;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasId()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasTimes()) {
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
        output.writeInt64(1, Id_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeInt32(2, times_);
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
          .computeInt64Size(1, Id_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, times_);
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
    
    public static GrandSonAreaTimes parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static GrandSonAreaTimes parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static GrandSonAreaTimes parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static GrandSonAreaTimes parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static GrandSonAreaTimes parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static GrandSonAreaTimes parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static GrandSonAreaTimes parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static GrandSonAreaTimes parseDelimitedFrom(
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
    public static GrandSonAreaTimes parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static GrandSonAreaTimes parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(GrandSonAreaTimes prototype) {
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
       implements GrandSonAreaTimesOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return LCGrandSonAreaTimes.internal_static_GrandSonAreaTimes_descriptor;
      }
      
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return LCGrandSonAreaTimes.internal_static_GrandSonAreaTimes_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCGrandSonAreaTimes.GrandSonAreaTimes.newBuilder()
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
        Id_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000001);
        times_ = 0;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return GrandSonAreaTimes.getDescriptor();
      }
      
      public GrandSonAreaTimes getDefaultInstanceForType() {
        return GrandSonAreaTimes.getDefaultInstance();
      }
      
      public GrandSonAreaTimes build() {
        GrandSonAreaTimes result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private GrandSonAreaTimes buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        GrandSonAreaTimes result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public GrandSonAreaTimes buildPartial() {
        GrandSonAreaTimes result = new GrandSonAreaTimes(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.Id_ = Id_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.times_ = times_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof GrandSonAreaTimes) {
          return mergeFrom((GrandSonAreaTimes)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(GrandSonAreaTimes other) {
        if (other == GrandSonAreaTimes.getDefaultInstance()) return this;
        if (other.hasId()) {
          setId(other.getId());
        }
        if (other.hasTimes()) {
          setTimes(other.getTimes());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasId()) {
          
          return false;
        }
        if (!hasTimes()) {
          
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
              Id_ = input.readInt64();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              times_ = input.readInt32();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required int64 _id = 1;
      private long Id_ ;
      public boolean hasId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public long getId() {
        return Id_;
      }
      public Builder setId(long value) {
        bitField0_ |= 0x00000001;
        Id_ = value;
        onChanged();
        return this;
      }
      public Builder clearId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        Id_ = 0L;
        onChanged();
        return this;
      }
      
      // required int32 times = 2;
      private int times_ ;
      public boolean hasTimes() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public int getTimes() {
        return times_;
      }
      public Builder setTimes(int value) {
        bitField0_ |= 0x00000002;
        times_ = value;
        onChanged();
        return this;
      }
      public Builder clearTimes() {
        bitField0_ = (bitField0_ & ~0x00000002);
        times_ = 0;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:GrandSonAreaTimes)
    }
    
    static {
      defaultInstance = new GrandSonAreaTimes(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:GrandSonAreaTimes)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_GrandSonAreaTimes_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_GrandSonAreaTimes_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\nCcore/proto/webservice/statisticsdata/e" +
      "ntity/GrandSonAreaTimes.proto\"/\n\021GrandSo" +
      "nAreaTimes\022\013\n\003_id\030\001 \002(\003\022\r\n\005times\030\002 \002(\005Bf" +
      "\nOcom.navinfo.opentsp.platform.location." +
      "protocol.webservice.statisticsdata.entit" +
      "yB\023LCGrandSonAreaTimes"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_GrandSonAreaTimes_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_GrandSonAreaTimes_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_GrandSonAreaTimes_descriptor,
              new String[] { "Id", "Times", },
              GrandSonAreaTimes.class,
              GrandSonAreaTimes.Builder.class);
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
