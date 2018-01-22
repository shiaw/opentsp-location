// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/terminal/monitor/java/EventReport.proto

package com.navinfo.opentsp.platform.location.protocol.terminal.monitor;

public final class LCEventReport {
  private LCEventReport() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface EventReportOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int32 eventId = 1;
    boolean hasEventId();
    int getEventId();
  }
  public static final class EventReport extends
      com.google.protobuf.GeneratedMessage
      implements EventReportOrBuilder {
    // Use EventReport.newBuilder() to construct.
    private EventReport(Builder builder) {
      super(builder);
    }
    private EventReport(boolean noInit) {}
    
    private static final EventReport defaultInstance;
    public static EventReport getDefaultInstance() {
      return defaultInstance;
    }
    
    public EventReport getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.internal_static_EventReport_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.internal_static_EventReport_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required int32 eventId = 1;
    public static final int EVENTID_FIELD_NUMBER = 1;
    private int eventId_;
    public boolean hasEventId() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public int getEventId() {
      return eventId_;
    }
    
    private void initFields() {
      eventId_ = 0;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasEventId()) {
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
        output.writeInt32(1, eventId_);
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
          .computeInt32Size(1, eventId_);
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
    
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport parseDelimitedFrom(
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
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport prototype) {
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
       implements com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReportOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.internal_static_EventReport_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.internal_static_EventReport_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport.newBuilder()
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
        eventId_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport.getDescriptor();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport getDefaultInstanceForType() {
        return com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport.getDefaultInstance();
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport build() {
        com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport buildPartial() {
        com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport result = new com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.eventId_ = eventId_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport) {
          return mergeFrom((com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport other) {
        if (other == com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport.getDefaultInstance()) return this;
        if (other.hasEventId()) {
          setEventId(other.getEventId());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasEventId()) {
          
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
              eventId_ = input.readInt32();
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required int32 eventId = 1;
      private int eventId_ ;
      public boolean hasEventId() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public int getEventId() {
        return eventId_;
      }
      public Builder setEventId(int value) {
        bitField0_ |= 0x00000001;
        eventId_ = value;
        onChanged();
        return this;
      }
      public Builder clearEventId() {
        bitField0_ = (bitField0_ & ~0x00000001);
        eventId_ = 0;
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:EventReport)
    }
    
    static {
      defaultInstance = new EventReport(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:EventReport)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_EventReport_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_EventReport_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n2core/proto/terminal/monitor/java/Event" +
      "Report.proto\"\036\n\013EventReport\022\017\n\007eventId\030\001" +
      " \002(\005BP\n?com.navinfo.opentsp.platform.loc" +
      "ation.protocol.terminal.monitorB\rLCEvent" +
      "Report"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_EventReport_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_EventReport_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_EventReport_descriptor,
              new java.lang.String[] { "EventId", },
              com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport.class,
              com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventReport.EventReport.Builder.class);
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