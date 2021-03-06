// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: core/proto/dataaccess/log/java/TaskInfoQuery.proto

package com.navinfo.opentsp.platform.location.protocol.dataaccess.log;

public final class LCTaskInfoQuery {
  private LCTaskInfoQuery() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface TaskInfoQueryOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required .TaskType types = 1;
    boolean hasTypes();
    com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskType.TaskType getTypes();
    
    // repeated int64 nodeCode = 2;
    java.util.List<Long> getNodeCodeList();
    int getNodeCodeCount();
    long getNodeCode(int index);
  }
  public static final class TaskInfoQuery extends
      com.google.protobuf.GeneratedMessage
      implements TaskInfoQueryOrBuilder {
    // Use TaskInfoQuery.newBuilder() to construct.
    private TaskInfoQuery(Builder builder) {
      super(builder);
    }
    private TaskInfoQuery(boolean noInit) {}

    private static final TaskInfoQuery defaultInstance;
    public static TaskInfoQuery getDefaultInstance() {
      return defaultInstance;
    }

    public TaskInfoQuery getDefaultInstanceForType() {
      return defaultInstance;
    }

    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.internal_static_TaskInfoQuery_descriptor;
    }

    protected FieldAccessorTable
        internalGetFieldAccessorTable() {
      return com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.internal_static_TaskInfoQuery_fieldAccessorTable;
    }

    private int bitField0_;
    // required .TaskType types = 1;
    public static final int TYPES_FIELD_NUMBER = 1;
    private com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskType.TaskType types_;
    public boolean hasTypes() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskType.TaskType getTypes() {
      return types_;
    }

    // repeated int64 nodeCode = 2;
    public static final int NODECODE_FIELD_NUMBER = 2;
    private java.util.List<Long> nodeCode_;
    public java.util.List<Long>
        getNodeCodeList() {
      return nodeCode_;
    }
    public int getNodeCodeCount() {
      return nodeCode_.size();
    }
    public long getNodeCode(int index) {
      return nodeCode_.get(index);
    }

    private void initFields() {
      types_ = com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskType.TaskType.DataTransfer;
      nodeCode_ = java.util.Collections.emptyList();;
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;

      if (!hasTypes()) {
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
        output.writeEnum(1, types_.getNumber());
      }
      for (int i = 0; i < nodeCode_.size(); i++) {
        output.writeInt64(2, nodeCode_.get(i));
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
          .computeEnumSize(1, types_.getNumber());
      }
      {
        int dataSize = 0;
        for (int i = 0; i < nodeCode_.size(); i++) {
          dataSize += com.google.protobuf.CodedOutputStream
            .computeInt64SizeNoTag(nodeCode_.get(i));
        }
        size += dataSize;
        size += 1 * getNodeCodeList().size();
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

    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery parseDelimitedFrom(
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
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }

    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery prototype) {
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
       implements com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQueryOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.internal_static_TaskInfoQuery_descriptor;
      }

      protected FieldAccessorTable
          internalGetFieldAccessorTable() {
        return com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.internal_static_TaskInfoQuery_fieldAccessorTable;
      }

      // Construct using com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery.newBuilder()
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
        types_ = com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskType.TaskType.DataTransfer;
        bitField0_ = (bitField0_ & ~0x00000001);
        nodeCode_ = java.util.Collections.emptyList();;
        bitField0_ = (bitField0_ & ~0x00000002);
        return this;
      }

      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery.getDescriptor();
      }

      public com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery getDefaultInstanceForType() {
        return com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery.getDefaultInstance();
      }

      public com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery build() {
        com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      private com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }

      public com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery buildPartial() {
        com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery result = new com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.types_ = types_;
        if (((bitField0_ & 0x00000002) == 0x00000002)) {
          nodeCode_ = java.util.Collections.unmodifiableList(nodeCode_);
          bitField0_ = (bitField0_ & ~0x00000002);
        }
        result.nodeCode_ = nodeCode_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery) {
          return mergeFrom((com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery other) {
        if (other == com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery.getDefaultInstance()) return this;
        if (other.hasTypes()) {
          setTypes(other.getTypes());
        }
        if (!other.nodeCode_.isEmpty()) {
          if (nodeCode_.isEmpty()) {
            nodeCode_ = other.nodeCode_;
            bitField0_ = (bitField0_ & ~0x00000002);
          } else {
            ensureNodeCodeIsMutable();
            nodeCode_.addAll(other.nodeCode_);
          }
          onChanged();
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }

      public final boolean isInitialized() {
        if (!hasTypes()) {

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
              int rawValue = input.readEnum();
              com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskType.TaskType value = com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskType.TaskType.valueOf(rawValue);
              if (value == null) {
                unknownFields.mergeVarintField(1, rawValue);
              } else {
                bitField0_ |= 0x00000001;
                types_ = value;
              }
              break;
            }
            case 16: {
              ensureNodeCodeIsMutable();
              nodeCode_.add(input.readInt64());
              break;
            }
            case 18: {
              int length = input.readRawVarint32();
              int limit = input.pushLimit(length);
              while (input.getBytesUntilLimit() > 0) {
                addNodeCode(input.readInt64());
              }
              input.popLimit(limit);
              break;
            }
          }
        }
      }

      private int bitField0_;

      // required .TaskType types = 1;
      private com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskType.TaskType types_ = com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskType.TaskType.DataTransfer;
      public boolean hasTypes() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskType.TaskType getTypes() {
        return types_;
      }
      public Builder setTypes(com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskType.TaskType value) {
        if (value == null) {
          throw new NullPointerException();
        }
        bitField0_ |= 0x00000001;
        types_ = value;
        onChanged();
        return this;
      }
      public Builder clearTypes() {
        bitField0_ = (bitField0_ & ~0x00000001);
        types_ = com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskType.TaskType.DataTransfer;
        onChanged();
        return this;
      }

      // repeated int64 nodeCode = 2;
      private java.util.List<Long> nodeCode_ = java.util.Collections.emptyList();;
      private void ensureNodeCodeIsMutable() {
        if (!((bitField0_ & 0x00000002) == 0x00000002)) {
          nodeCode_ = new java.util.ArrayList<Long>(nodeCode_);
          bitField0_ |= 0x00000002;
         }
      }
      public java.util.List<Long>
          getNodeCodeList() {
        return java.util.Collections.unmodifiableList(nodeCode_);
      }
      public int getNodeCodeCount() {
        return nodeCode_.size();
      }
      public long getNodeCode(int index) {
        return nodeCode_.get(index);
      }
      public Builder setNodeCode(
          int index, long value) {
        ensureNodeCodeIsMutable();
        nodeCode_.set(index, value);
        onChanged();
        return this;
      }
      public Builder addNodeCode(long value) {
        ensureNodeCodeIsMutable();
        nodeCode_.add(value);
        onChanged();
        return this;
      }
      public Builder addAllNodeCode(
          Iterable<? extends Long> values) {
        ensureNodeCodeIsMutable();
        super.addAll(values, nodeCode_);
        onChanged();
        return this;
      }
      public Builder clearNodeCode() {
        nodeCode_ = java.util.Collections.emptyList();;
        bitField0_ = (bitField0_ & ~0x00000002);
        onChanged();
        return this;
      }

      // @@protoc_insertion_point(builder_scope:TaskInfoQuery)
    }

    static {
      defaultInstance = new TaskInfoQuery(true);
      defaultInstance.initFields();
    }

    // @@protoc_insertion_point(class_scope:TaskInfoQuery)
  }

  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_TaskInfoQuery_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_TaskInfoQuery_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    String[] descriptorData = {
      "\n2core/proto/dataaccess/log/java/TaskInf" +
      "oQuery.proto\0320core/proto/dataaccess/comm" +
      "on/java/TaskType.proto\";\n\rTaskInfoQuery\022" +
      "\030\n\005types\030\001 \002(\0162\t.TaskType\022\020\n\010nodeCode\030\002 " +
      "\003(\003BP\n=com.navinfo.opentsp.platform.loca" +
      "tion.protocol.dataaccess.logB\017LCTaskInfo" +
      "Query"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_TaskInfoQuery_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_TaskInfoQuery_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_TaskInfoQuery_descriptor,
              new String[] { "Types", "NodeCode", },
              com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery.class,
              com.navinfo.opentsp.platform.location.protocol.dataaccess.log.LCTaskInfoQuery.TaskInfoQuery.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCTaskType.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
