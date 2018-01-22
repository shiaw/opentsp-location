// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/dataaccess/common/java/TaskInfo.proto

package com.navinfo.opentsp.platform.location.protocol.dataaccess.common;

public final class LCTaskInfo {
  private LCTaskInfo() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface TaskInfoOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int64 nodeCode = 1;
    boolean hasNodeCode();
    long getNodeCode();
    
    // repeated int64 terminalId = 2;
    java.util.List<Long> getTerminalIdList();
    int getTerminalIdCount();
    long getTerminalId(int index);
  }
  public static final class TaskInfo extends
      com.google.protobuf.GeneratedMessage
      implements TaskInfoOrBuilder {
    // Use TaskInfo.newBuilder() to construct.
    private TaskInfo(Builder builder) {
      super(builder);
    }
    private TaskInfo(boolean noInit) {}
    
    private static final TaskInfo defaultInstance;
    public static TaskInfo getDefaultInstance() {
      return defaultInstance;
    }
    
    public TaskInfo getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return LCTaskInfo.internal_static_TaskInfo_descriptor;
    }
    
    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return LCTaskInfo.internal_static_TaskInfo_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required int64 nodeCode = 1;
    public static final int NODECODE_FIELD_NUMBER = 1;
    private long nodeCode_;
    public boolean hasNodeCode() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public long getNodeCode() {
      return nodeCode_;
    }
    
    // repeated int64 terminalId = 2;
    public static final int TERMINALID_FIELD_NUMBER = 2;
    private java.util.List<Long> terminalId_;
    public java.util.List<Long>
        getTerminalIdList() {
      return terminalId_;
    }
    public int getTerminalIdCount() {
      return terminalId_.size();
    }
    public long getTerminalId(int index) {
      return terminalId_.get(index);
    }
    
    private void initFields() {
      nodeCode_ = 0L;
      terminalId_ = java.util.Collections.emptyList();;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasNodeCode()) {
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
        output.writeInt64(1, nodeCode_);
      }
      for (int i = 0; i < terminalId_.size(); i++) {
        output.writeInt64(2, terminalId_.get(i));
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
          .computeInt64Size(1, nodeCode_);
      }
      {
        int dataSize = 0;
        for (int i = 0; i < terminalId_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeInt64SizeNoTag(terminalId_.get(i));
        }
        size += dataSize;
        size += 1 * getTerminalIdList().size();
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
    
    public static TaskInfo parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static TaskInfo parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static TaskInfo parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static TaskInfo parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static TaskInfo parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static TaskInfo parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static TaskInfo parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static TaskInfo parseDelimitedFrom(
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
    public static TaskInfo parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static TaskInfo parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(TaskInfo prototype) {
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
       implements TaskInfoOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return LCTaskInfo.internal_static_TaskInfo_descriptor;
      }
      
      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return LCTaskInfo.internal_static_TaskInfo_fieldAccessorTable;
      }
      
      // Construct using com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskInfo.TaskInfo.newBuilder()
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
        nodeCode_ = 0L;
        bitField0_ = (bitField0_ & ~0x00000001);
        terminalId_ = java.util.Collections.emptyList();;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return TaskInfo.getDescriptor();
      }
      
      public TaskInfo getDefaultInstanceForType() {
        return TaskInfo.getDefaultInstance();
      }
      
      public TaskInfo build() {
        TaskInfo result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private TaskInfo buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        TaskInfo result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public TaskInfo buildPartial() {
        TaskInfo result = new TaskInfo(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.nodeCode_ = nodeCode_;
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          terminalId_ = java.util.Collections.unmodifiableList(terminalId_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.terminalId_ = terminalId_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof TaskInfo) {
          return mergeFrom((TaskInfo)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(TaskInfo other) {
        if (other == TaskInfo.getDefaultInstance()) return this;
        if (other.hasNodeCode()) {
          setNodeCode(other.getNodeCode());
        }
        if (!other.terminalId_.isEmpty()) {
          if (terminalId_.isEmpty()) {
            terminalId_ = other.terminalId_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureTerminalIdIsMutable();
            terminalId_.addAll(other.terminalId_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasNodeCode()) {
          
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
              nodeCode_ = input.readInt64();
              break;
            }
            case 16: {
              ensureTerminalIdIsMutable();
              terminalId_.add(input.readInt64());
              break;
            }
            case 18: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              while (input.getBytesUntilLimit() > 0) {
                addTerminalId(input.readInt64());
              }
              input.popLimit(limit);
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required int64 nodeCode = 1;
      private long nodeCode_ ;
      public boolean hasNodeCode() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public long getNodeCode() {
        return nodeCode_;
      }
      public Builder setNodeCode(long value) {
        bitField0_ |= 0x00000001;
        nodeCode_ = value;
        onChanged();
        return this;
      }
      public Builder clearNodeCode() {
        bitField0_ = (bitField0_ & ~0x00000001);
        nodeCode_ = 0L;
        onChanged();
        return this;
      }
      
      // repeated int64 terminalId = 2;
      private java.util.List<Long> terminalId_ = java.util.Collections.emptyList();;
      private void ensureTerminalIdIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          terminalId_ = new java.util.ArrayList<Long>(terminalId_);
          bitField0_ |= 0x00000002;
         }
      }
      public java.util.List<Long>
          getTerminalIdList() {
        return java.util.Collections.unmodifiableList(terminalId_);
      }
      public int getTerminalIdCount() {
        return terminalId_.size();
      }
      public long getTerminalId(int index) {
        return terminalId_.get(index);
      }
      public Builder setTerminalId(
          int index, long value) {
        ensureTerminalIdIsMutable();
        terminalId_.set(index, value);
        onChanged();
        return this;
      }
      public Builder addTerminalId(long value) {
        ensureTerminalIdIsMutable();
        terminalId_.add(value);
        onChanged();
        return this;
      }
      public Builder addAllTerminalId(
          Iterable<? extends Long> values) {
        ensureTerminalIdIsMutable();
        super.addAll(values, terminalId_);
        onChanged();
        return this;
      }
      public Builder clearTerminalId() {
        terminalId_ = java.util.Collections.emptyList();;
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }
      
      // @@protoc_insertion_point(builder_scope:TaskInfo)
    }
    
    static {
      defaultInstance = new TaskInfo(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:TaskInfo)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_TaskInfo_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_TaskInfo_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n0core/proto/dataaccess/common/java/Task" +
      "Info.proto\"0\n\010TaskInfo\022\020\n\010nodeCode\030\001 \002(\003" +
      "\022\022\n\nterminalId\030\002 \003(\003BN\n@com.navinfo.open" +
      "tsp.platform.location.protocol.dataacces" +
      "s.commonB\nLCTaskInfo"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_TaskInfo_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_TaskInfo_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_TaskInfo_descriptor,
              new String[] { "NodeCode", "TerminalId", },
              TaskInfo.class,
              TaskInfo.Builder.class);
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