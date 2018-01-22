// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/nodecluster/java/DSAFeedbackStatisticResult.proto

package com.navinfo.opentsp.platform.location.protocol.nodecluster;

public final class LCDSAFeedbackStatisticResult {
  private LCDSAFeedbackStatisticResult() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface DSAFeedbackStatisticResultOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int64 endedTime = 1;
    boolean hasEndedTime();
    long getEndedTime();
    
    // required bool isCompleted = 2;
    boolean hasIsCompleted();
    boolean getIsCompleted();
    
    // repeated int64 archivedTerminals = 3;
    java.util.List<Long> getArchivedTerminalsList();
    int getArchivedTerminalsCount();
    long getArchivedTerminals(int index);
  }
  public static final class DSAFeedbackStatisticResult extends
      com.google.protobuf.GeneratedMessage
      implements DSAFeedbackStatisticResultOrBuilder {
    // Use DSAFeedbackStatisticResult.newBuilder() to construct.
    private DSAFeedbackStatisticResult(Builder builder) {
      super(builder);
    }
    private DSAFeedbackStatisticResult(boolean noInit) {}
    
    private static final DSAFeedbackStatisticResult defaultInstance;
    public static DSAFeedbackStatisticResult getDefaultInstance() {
      return defaultInstance;
    }
    
    public DSAFeedbackStatisticResult getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return LCDSAFeedbackStatisticResult.internal_static_DSAFeedbackStatisticResult_descriptor;
    }
    
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return LCDSAFeedbackStatisticResult.internal_static_DSAFeedbackStatisticResult_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required int64 endedTime = 1;
    public static final int ENDEDTIME_FIELD_NUMBER = 1;
    private long endedTime_;
    public boolean hasEndedTime() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public long getEndedTime() {
      return endedTime_;
    }
    
    // required bool isCompleted = 2;
    public static final int ISCOMPLETED_FIELD_NUMBER = 2;
    private boolean isCompleted_;
    public boolean hasIsCompleted() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public boolean getIsCompleted() {
      return isCompleted_;
    }
    
    // repeated int64 archivedTerminals = 3;
    public static final int ARCHIVEDTERMINALS_FIELD_NUMBER = 3;
    private java.util.List<Long> archivedTerminals_;
    public java.util.List<Long>
        getArchivedTerminalsList() {
      return archivedTerminals_;
    }
    public int getArchivedTerminalsCount() {
      return archivedTerminals_.size();
    }
    public long getArchivedTerminals(int index) {
      return archivedTerminals_.get(index);
    }
    
    private void initFields() {
      endedTime_ = 0L;
      isCompleted_ = false;
      archivedTerminals_ = java.util.Collections.emptyList();;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasEndedTime()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasIsCompleted()) {
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
        output.writeInt64(1, endedTime_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBool(2, isCompleted_);
      }
      for (int i = 0; i < archivedTerminals_.size(); i++) {
        output.writeInt64(3, archivedTerminals_.get(i));
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
          .computeInt64Size(1, endedTime_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBoolSize(2, isCompleted_);
      }
      {
        int dataSize = 0;
        for (int i = 0; i < archivedTerminals_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeInt64SizeNoTag(archivedTerminals_.get(i));
        }
        size += dataSize;
        size += 1 * getArchivedTerminalsList().size();
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
    
    public static DSAFeedbackStatisticResult parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static DSAFeedbackStatisticResult parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static DSAFeedbackStatisticResult parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static DSAFeedbackStatisticResult parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static DSAFeedbackStatisticResult parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static DSAFeedbackStatisticResult parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static DSAFeedbackStatisticResult parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static DSAFeedbackStatisticResult parseDelimitedFrom(
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
    public static DSAFeedbackStatisticResult parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static DSAFeedbackStatisticResult parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(DSAFeedbackStatisticResult prototype) {
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
       implements DSAFeedbackStatisticResultOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return LCDSAFeedbackStatisticResult.internal_static_DSAFeedbackStatisticResult_descriptor;
      }
      
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return LCDSAFeedbackStatisticResult.internal_static_DSAFeedbackStatisticResult_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.nodecluster.LCDSAFeedbackStatisticResult.DSAFeedbackStatisticResult.newBuilder()
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
        endedTime_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000001);
        isCompleted_ = false;
        bitField0_ = (bitField0_ & ~0x00000002);
        archivedTerminals_ = java.util.Collections.emptyList();;
        bitField0_ = (bitField0_ & ~0x00000004);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return DSAFeedbackStatisticResult.getDescriptor();
      }
      
      public DSAFeedbackStatisticResult getDefaultInstanceForType() {
        return DSAFeedbackStatisticResult.getDefaultInstance();
      }
      
      public DSAFeedbackStatisticResult build() {
        DSAFeedbackStatisticResult result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private DSAFeedbackStatisticResult buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        DSAFeedbackStatisticResult result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public DSAFeedbackStatisticResult buildPartial() {
        DSAFeedbackStatisticResult result = new DSAFeedbackStatisticResult(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.endedTime_ = endedTime_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.isCompleted_ = isCompleted_;
        if (((bitField0_ & 0x00000004) == 0x00000004)) {
          archivedTerminals_ = java.util.Collections.unmodifiableList(archivedTerminals_);
          bitField0_ = (bitField0_ & ~0x00000004);
        }
        result.archivedTerminals_ = archivedTerminals_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof DSAFeedbackStatisticResult) {
          return mergeFrom((DSAFeedbackStatisticResult)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(DSAFeedbackStatisticResult other) {
        if (other == DSAFeedbackStatisticResult.getDefaultInstance()) return this;
        if (other.hasEndedTime()) {
          setEndedTime(other.getEndedTime());
        }
        if (other.hasIsCompleted()) {
          setIsCompleted(other.getIsCompleted());
        }
        if (!other.archivedTerminals_.isEmpty()) {
          if (archivedTerminals_.isEmpty()) {
            archivedTerminals_ = other.archivedTerminals_;
            bitField0_ = (bitField0_ & ~0x00000004);
          } else {
            ensureArchivedTerminalsIsMutable();
            archivedTerminals_.addAll(other.archivedTerminals_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasEndedTime()) {
          
          return false;
        }
        if (!hasIsCompleted()) {
          
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
              endedTime_ = input.readInt64();
              break;
            }
            case 16: {
              bitField0_ |= 0x00000002;
              isCompleted_ = input.readBool();
              break;
            }
            case 24: {
              ensureArchivedTerminalsIsMutable();
              archivedTerminals_.add(input.readInt64());
              break;
            }
            case 26: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              while (input.getBytesUntilLimit() > 0) {
                addArchivedTerminals(input.readInt64());
              }
              input.popLimit(limit);
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required int64 endedTime = 1;
      private long endedTime_ ;
      public boolean hasEndedTime() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public long getEndedTime() {
        return endedTime_;
      }
      public Builder setEndedTime(long value) {
        bitField0_ |= 0x00000001;
        endedTime_ = value;
        onChanged();
        return this;
      }
      public Builder clearEndedTime() {
        bitField0_ = (bitField0_ & ~0x00000001);
        endedTime_ = 0L;
        onChanged();
        return this;
      }
      
      // required bool isCompleted = 2;
      private boolean isCompleted_ ;
      public boolean hasIsCompleted() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public boolean getIsCompleted() {
        return isCompleted_;
      }
      public Builder setIsCompleted(boolean value) {
        bitField0_ |= 0x00000002;
        isCompleted_ = value;
        onChanged();
        return this;
      }
      public Builder clearIsCompleted() {
        bitField0_ = (bitField0_ & ~0x00000002);
        isCompleted_ = false;
        onChanged();
        return this;
      }
      
      // repeated int64 archivedTerminals = 3;
      private java.util.List<Long> archivedTerminals_ = java.util.Collections.emptyList();;
      private void ensureArchivedTerminalsIsMutable() {
        if (!((bitField0_ & 0x00000004) == 0x00000004)) {
          archivedTerminals_ = new java.util.ArrayList<Long>(archivedTerminals_);
          bitField0_ |= 0x00000004;
         }
      }
      public java.util.List<Long>
          getArchivedTerminalsList() {
        return java.util.Collections.unmodifiableList(archivedTerminals_);
      }
      public int getArchivedTerminalsCount() {
        return archivedTerminals_.size();
      }
      public long getArchivedTerminals(int index) {
        return archivedTerminals_.get(index);
      }
      public Builder setArchivedTerminals(
          int index, long value) {
        ensureArchivedTerminalsIsMutable();
        archivedTerminals_.set(index, value);
        onChanged();
        return this;
      }
      public Builder addArchivedTerminals(long value) {
        ensureArchivedTerminalsIsMutable();
        archivedTerminals_.add(value);
        onChanged();
        return this;
      }
      public Builder addAllArchivedTerminals(
          Iterable<? extends Long> values) {
        ensureArchivedTerminalsIsMutable();
        super.addAll(values, archivedTerminals_);
        onChanged();
        return this;
      }
      public Builder clearArchivedTerminals() {
        archivedTerminals_ = java.util.Collections.emptyList();;
        bitField0_ = (bitField0_ & ~0x00000004);
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:DSAFeedbackStatisticResult)
    }
    
    static {
      defaultInstance = new DSAFeedbackStatisticResult(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:DSAFeedbackStatisticResult)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_DSAFeedbackStatisticResult_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_DSAFeedbackStatisticResult_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n<core/proto/nodecluster/java/DSAFeedbac" +
      "kStatisticResult.proto\"_\n\032DSAFeedbackSta" +
      "tisticResult\022\021\n\tendedTime\030\001 \002(\003\022\023\n\013isCom" +
      "pleted\030\002 \002(\010\022\031\n\021archivedTerminals\030\003 \003(\003B" +
      "Z\n:com.navinfo.opentsp.platform.location" +
      ".protocol.nodeclusterB\034LCDSAFeedbackStat" +
      "isticResult"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_DSAFeedbackStatisticResult_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_DSAFeedbackStatisticResult_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_DSAFeedbackStatisticResult_descriptor,
              new String[] { "EndedTime", "IsCompleted", "ArchivedTerminals", },
              DSAFeedbackStatisticResult.class,
              DSAFeedbackStatisticResult.Builder.class);
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