// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/terminal/query/travelrecorder/TRSetInstallDate.proto

package com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder;

public final class LCTRSetInstallDate {
  private LCTRSetInstallDate() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface TRSetInstallDateOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int64 currentDate = 1;
    boolean hasCurrentDate();
    long getCurrentDate();
  }
  public static final class TRSetInstallDate extends
      com.google.protobuf.GeneratedMessage
      implements TRSetInstallDateOrBuilder {
    // Use TRSetInstallDate.newBuilder() to construct.
    private TRSetInstallDate(Builder builder) {
      super(builder);
    }
    private TRSetInstallDate(boolean noInit) {}
    
    private static final TRSetInstallDate defaultInstance;
    public static TRSetInstallDate getDefaultInstance() {
      return defaultInstance;
    }
    
    public TRSetInstallDate getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.internal_static_TRSetInstallDate_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.internal_static_TRSetInstallDate_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required int64 currentDate = 1;
    public static final int CURRENTDATE_FIELD_NUMBER = 1;
    private long currentDate_;
    public boolean hasCurrentDate() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public long getCurrentDate() {
      return currentDate_;
    }
    
    private void initFields() {
      currentDate_ = 0L;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasCurrentDate()) {
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
        output.writeInt64(1, currentDate_);
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
          .computeInt64Size(1, currentDate_);
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
    
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate parseDelimitedFrom(
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
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate prototype) {
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
       implements com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDateOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.internal_static_TRSetInstallDate_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.internal_static_TRSetInstallDate_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate.newBuilder()
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
        currentDate_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate.getDescriptor();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate getDefaultInstanceForType() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate.getDefaultInstance();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate build() {
        com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate buildPartial() {
        com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate result = new com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.currentDate_ = currentDate_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate) {
          return mergeFrom((com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate other) {
        if (other == com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate.getDefaultInstance()) return this;
        if (other.hasCurrentDate()) {
          setCurrentDate(other.getCurrentDate());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasCurrentDate()) {
          
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
              currentDate_ = input.readInt64();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required int64 currentDate = 1;
      private long currentDate_ ;
      public boolean hasCurrentDate() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public long getCurrentDate() {
        return currentDate_;
      }
      public Builder setCurrentDate(long value) {
        bitField0_ |= 0x00000001;
        currentDate_ = value;
        onChanged();
        return this;
      }
      public Builder clearCurrentDate() {
        bitField0_ = (bitField0_ & ~0x00000001);
        currentDate_ = 0L;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:TRSetInstallDate)
    }
    
    static {
      defaultInstance = new TRSetInstallDate(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:TRSetInstallDate)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_TRSetInstallDate_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_TRSetInstallDate_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n?core/proto/terminal/query/travelrecord" +
      "er/TRSetInstallDate.proto\"\'\n\020TRSetInstal" +
      "lDate\022\023\n\013currentDate\030\001 \002(\003Bb\nLcom.navinf" +
      "o.opentsp.platform.location.protocol.ter" +
      "minal.query.travelrecorderB\022LCTRSetInsta" +
      "llDate"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_TRSetInstallDate_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_TRSetInstallDate_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_TRSetInstallDate_descriptor,
              new java.lang.String[] { "CurrentDate", },
              com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate.class,
              com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSetInstallDate.TRSetInstallDate.Builder.class);
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
