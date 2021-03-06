// Generated by ProtoGen, Version=2.4.1.473, Culture=neutral, PublicKeyToken=55f7125234beb589.  DO NOT EDIT!
#pragma warning disable 1591, 0612
#region Designer generated code

using pb = global::Google.ProtocolBuffers;
using pbc = global::Google.ProtocolBuffers.Collections;
using pbd = global::Google.ProtocolBuffers.Descriptors;
using scg = global::System.Collections.Generic;
namespace AeroCloud.Protocol {
  
  namespace Proto {
    
    [global::System.Diagnostics.DebuggerNonUserCodeAttribute()]
    [global::System.Runtime.CompilerServices.CompilerGeneratedAttribute()]
    [global::System.CodeDom.Compiler.GeneratedCodeAttribute("ProtoGen", "2.4.1.473")]
    public static partial class RouteDriverTime {
    
      #region Extension registration
      public static void RegisterAllExtensions(pb::ExtensionRegistry registry) {
      }
      #endregion
      #region Static variables
      internal static pbd::MessageDescriptor internal__static_RouteDriverTime__Descriptor;
      internal static pb::FieldAccess.FieldAccessorTable<global::AeroCloud.Protocol.RouteDriverTime, global::AeroCloud.Protocol.RouteDriverTime.Builder> internal__static_RouteDriverTime__FieldAccessorTable;
      #endregion
      #region Descriptor
      public static pbd::FileDescriptor Descriptor {
        get { return descriptor; }
      }
      private static pbd::FileDescriptor descriptor;
      
      static RouteDriverTime() {
        byte[] descriptorData = global::System.Convert.FromBase64String(
            "CjZjb3JlL3Byb3RvL2RhdGFhY2Nlc3MvY29tbW9uL25ldC9Sb3V0ZURyaXZl" + 
            "clRpbWUucHJvdG8aFGNzaGFycF9vcHRpb25zLnByb3RvIp8BCg9Sb3V0ZURy" + 
            "aXZlclRpbWUSDwoHcm91dGVJZBgBIAIoAxIOCgZsaW5lSWQYAiACKAMSDwoH" + 
            "bWF4VGltZRgDIAIoBRIPCgdtaW5UaW1lGAQgAigFEhEKCWJhc2VkVGltZRgF" + 
            "IAIoCBISCgppc0V2ZXJ5RGF5GAYgASgIEhEKCXN0YXJ0RGF0ZRgHIAEoAxIP" + 
            "CgdlbmREYXRlGAggASgDQlIKJmNvbS5sYy5jb3JlLnByb3RvY29sLmRhdGFh" + 
            "Y2Nlc3MuY29tbW9uQhFMQ1JvdXRlRHJpdmVyVGltZcI+FAoSQWVyb0Nsb3Vk" + 
            "LlByb3RvY29s");
        pbd::FileDescriptor.InternalDescriptorAssigner assigner = delegate(pbd::FileDescriptor root) {
          descriptor = root;
          internal__static_RouteDriverTime__Descriptor = Descriptor.MessageTypes[0];
          internal__static_RouteDriverTime__FieldAccessorTable = 
              new pb::FieldAccess.FieldAccessorTable<global::AeroCloud.Protocol.RouteDriverTime, global::AeroCloud.Protocol.RouteDriverTime.Builder>(internal__static_RouteDriverTime__Descriptor,
                  new string[] { "RouteId", "LineId", "MaxTime", "MinTime", "BasedTime", "IsEveryDay", "StartDate", "EndDate", });
          pb::ExtensionRegistry registry = pb::ExtensionRegistry.CreateInstance();
          RegisterAllExtensions(registry);
          global::Google.ProtocolBuffers.DescriptorProtos.CSharpOptions.RegisterAllExtensions(registry);
          return registry;
        };
        pbd::FileDescriptor.InternalBuildGeneratedFileFrom(descriptorData,
            new pbd::FileDescriptor[] {
            global::Google.ProtocolBuffers.DescriptorProtos.CSharpOptions.Descriptor, 
            }, assigner);
      }
      #endregion
      
    }
  }
  #region Messages
  [global::System.Diagnostics.DebuggerNonUserCodeAttribute()]
  [global::System.Runtime.CompilerServices.CompilerGeneratedAttribute()]
  [global::System.CodeDom.Compiler.GeneratedCodeAttribute("ProtoGen", "2.4.1.473")]
  public sealed partial class RouteDriverTime : pb::GeneratedMessage<RouteDriverTime, RouteDriverTime.Builder> {
    private RouteDriverTime() { }
    private static readonly RouteDriverTime defaultInstance = new RouteDriverTime().MakeReadOnly();
    private static readonly string[] _routeDriverTimeFieldNames = new string[] { "basedTime", "endDate", "isEveryDay", "lineId", "maxTime", "minTime", "routeId", "startDate" };
    private static readonly uint[] _routeDriverTimeFieldTags = new uint[] { 40, 64, 48, 16, 24, 32, 8, 56 };
    public static RouteDriverTime DefaultInstance {
      get { return defaultInstance; }
    }
    
    public override RouteDriverTime DefaultInstanceForType {
      get { return DefaultInstance; }
    }
    
    protected override RouteDriverTime ThisMessage {
      get { return this; }
    }
    
    public static pbd::MessageDescriptor Descriptor {
      get { return global::AeroCloud.Protocol.Proto.RouteDriverTime.internal__static_RouteDriverTime__Descriptor; }
    }
    
    protected override pb::FieldAccess.FieldAccessorTable<RouteDriverTime, RouteDriverTime.Builder> InternalFieldAccessors {
      get { return global::AeroCloud.Protocol.Proto.RouteDriverTime.internal__static_RouteDriverTime__FieldAccessorTable; }
    }
    
    public const int RouteIdFieldNumber = 1;
    private bool hasRouteId;
    private long routeId_;
    public bool HasRouteId {
      get { return hasRouteId; }
    }
    public long RouteId {
      get { return routeId_; }
    }
    
    public const int LineIdFieldNumber = 2;
    private bool hasLineId;
    private long lineId_;
    public bool HasLineId {
      get { return hasLineId; }
    }
    public long LineId {
      get { return lineId_; }
    }
    
    public const int MaxTimeFieldNumber = 3;
    private bool hasMaxTime;
    private int maxTime_;
    public bool HasMaxTime {
      get { return hasMaxTime; }
    }
    public int MaxTime {
      get { return maxTime_; }
    }
    
    public const int MinTimeFieldNumber = 4;
    private bool hasMinTime;
    private int minTime_;
    public bool HasMinTime {
      get { return hasMinTime; }
    }
    public int MinTime {
      get { return minTime_; }
    }
    
    public const int BasedTimeFieldNumber = 5;
    private bool hasBasedTime;
    private bool basedTime_;
    public bool HasBasedTime {
      get { return hasBasedTime; }
    }
    public bool BasedTime {
      get { return basedTime_; }
    }
    
    public const int IsEveryDayFieldNumber = 6;
    private bool hasIsEveryDay;
    private bool isEveryDay_;
    public bool HasIsEveryDay {
      get { return hasIsEveryDay; }
    }
    public bool IsEveryDay {
      get { return isEveryDay_; }
    }
    
    public const int StartDateFieldNumber = 7;
    private bool hasStartDate;
    private long startDate_;
    public bool HasStartDate {
      get { return hasStartDate; }
    }
    public long StartDate {
      get { return startDate_; }
    }
    
    public const int EndDateFieldNumber = 8;
    private bool hasEndDate;
    private long endDate_;
    public bool HasEndDate {
      get { return hasEndDate; }
    }
    public long EndDate {
      get { return endDate_; }
    }
    
    public override bool IsInitialized {
      get {
        if (!hasRouteId) return false;
        if (!hasLineId) return false;
        if (!hasMaxTime) return false;
        if (!hasMinTime) return false;
        if (!hasBasedTime) return false;
        return true;
      }
    }
    
    public override void WriteTo(pb::ICodedOutputStream output) {
      int size = SerializedSize;
      string[] field_names = _routeDriverTimeFieldNames;
      if (hasRouteId) {
        output.WriteInt64(1, field_names[6], RouteId);
      }
      if (hasLineId) {
        output.WriteInt64(2, field_names[3], LineId);
      }
      if (hasMaxTime) {
        output.WriteInt32(3, field_names[4], MaxTime);
      }
      if (hasMinTime) {
        output.WriteInt32(4, field_names[5], MinTime);
      }
      if (hasBasedTime) {
        output.WriteBool(5, field_names[0], BasedTime);
      }
      if (hasIsEveryDay) {
        output.WriteBool(6, field_names[2], IsEveryDay);
      }
      if (hasStartDate) {
        output.WriteInt64(7, field_names[7], StartDate);
      }
      if (hasEndDate) {
        output.WriteInt64(8, field_names[1], EndDate);
      }
      UnknownFields.WriteTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public override int SerializedSize {
      get {
        int size = memoizedSerializedSize;
        if (size != -1) return size;
        
        size = 0;
        if (hasRouteId) {
          size += pb::CodedOutputStream.ComputeInt64Size(1, RouteId);
        }
        if (hasLineId) {
          size += pb::CodedOutputStream.ComputeInt64Size(2, LineId);
        }
        if (hasMaxTime) {
          size += pb::CodedOutputStream.ComputeInt32Size(3, MaxTime);
        }
        if (hasMinTime) {
          size += pb::CodedOutputStream.ComputeInt32Size(4, MinTime);
        }
        if (hasBasedTime) {
          size += pb::CodedOutputStream.ComputeBoolSize(5, BasedTime);
        }
        if (hasIsEveryDay) {
          size += pb::CodedOutputStream.ComputeBoolSize(6, IsEveryDay);
        }
        if (hasStartDate) {
          size += pb::CodedOutputStream.ComputeInt64Size(7, StartDate);
        }
        if (hasEndDate) {
          size += pb::CodedOutputStream.ComputeInt64Size(8, EndDate);
        }
        size += UnknownFields.SerializedSize;
        memoizedSerializedSize = size;
        return size;
      }
    }
    
    public static RouteDriverTime ParseFrom(pb::ByteString data) {
      return ((Builder) CreateBuilder().MergeFrom(data)).BuildParsed();
    }
    public static RouteDriverTime ParseFrom(pb::ByteString data, pb::ExtensionRegistry extensionRegistry) {
      return ((Builder) CreateBuilder().MergeFrom(data, extensionRegistry)).BuildParsed();
    }
    public static RouteDriverTime ParseFrom(byte[] data) {
      return ((Builder) CreateBuilder().MergeFrom(data)).BuildParsed();
    }
    public static RouteDriverTime ParseFrom(byte[] data, pb::ExtensionRegistry extensionRegistry) {
      return ((Builder) CreateBuilder().MergeFrom(data, extensionRegistry)).BuildParsed();
    }
    public static RouteDriverTime ParseFrom(global::System.IO.Stream input) {
      return ((Builder) CreateBuilder().MergeFrom(input)).BuildParsed();
    }
    public static RouteDriverTime ParseFrom(global::System.IO.Stream input, pb::ExtensionRegistry extensionRegistry) {
      return ((Builder) CreateBuilder().MergeFrom(input, extensionRegistry)).BuildParsed();
    }
    public static RouteDriverTime ParseDelimitedFrom(global::System.IO.Stream input) {
      return CreateBuilder().MergeDelimitedFrom(input).BuildParsed();
    }
    public static RouteDriverTime ParseDelimitedFrom(global::System.IO.Stream input, pb::ExtensionRegistry extensionRegistry) {
      return CreateBuilder().MergeDelimitedFrom(input, extensionRegistry).BuildParsed();
    }
    public static RouteDriverTime ParseFrom(pb::ICodedInputStream input) {
      return ((Builder) CreateBuilder().MergeFrom(input)).BuildParsed();
    }
    public static RouteDriverTime ParseFrom(pb::ICodedInputStream input, pb::ExtensionRegistry extensionRegistry) {
      return ((Builder) CreateBuilder().MergeFrom(input, extensionRegistry)).BuildParsed();
    }
    private RouteDriverTime MakeReadOnly() {
      return this;
    }
    
    public static Builder CreateBuilder() { return new Builder(); }
    public override Builder ToBuilder() { return CreateBuilder(this); }
    public override Builder CreateBuilderForType() { return new Builder(); }
    public static Builder CreateBuilder(RouteDriverTime prototype) {
      return new Builder(prototype);
    }
    
    [global::System.Diagnostics.DebuggerNonUserCodeAttribute()]
    [global::System.Runtime.CompilerServices.CompilerGeneratedAttribute()]
    [global::System.CodeDom.Compiler.GeneratedCodeAttribute("ProtoGen", "2.4.1.473")]
    public sealed partial class Builder : pb::GeneratedBuilder<RouteDriverTime, Builder> {
      protected override Builder ThisBuilder {
        get { return this; }
      }
      public Builder() {
        result = DefaultInstance;
        resultIsReadOnly = true;
      }
      internal Builder(RouteDriverTime cloneFrom) {
        result = cloneFrom;
        resultIsReadOnly = true;
      }
      
      private bool resultIsReadOnly;
      private RouteDriverTime result;
      
      private RouteDriverTime PrepareBuilder() {
        if (resultIsReadOnly) {
          RouteDriverTime original = result;
          result = new RouteDriverTime();
          resultIsReadOnly = false;
          MergeFrom(original);
        }
        return result;
      }
      
      public override bool IsInitialized {
        get { return result.IsInitialized; }
      }
      
      protected override RouteDriverTime MessageBeingBuilt {
        get { return PrepareBuilder(); }
      }
      
      public override Builder Clear() {
        result = DefaultInstance;
        resultIsReadOnly = true;
        return this;
      }
      
      public override Builder Clone() {
        if (resultIsReadOnly) {
          return new Builder(result);
        } else {
          return new Builder().MergeFrom(result);
        }
      }
      
      public override pbd::MessageDescriptor DescriptorForType {
        get { return global::AeroCloud.Protocol.RouteDriverTime.Descriptor; }
      }
      
      public override RouteDriverTime DefaultInstanceForType {
        get { return global::AeroCloud.Protocol.RouteDriverTime.DefaultInstance; }
      }
      
      public override RouteDriverTime BuildPartial() {
        if (resultIsReadOnly) {
          return result;
        }
        resultIsReadOnly = true;
        return result.MakeReadOnly();
      }
      
      public override Builder MergeFrom(pb::IMessage other) {
        if (other is RouteDriverTime) {
          return MergeFrom((RouteDriverTime) other);
        } else {
          base.MergeFrom(other);
          return this;
        }
      }
      
      public override Builder MergeFrom(RouteDriverTime other) {
        if (other == global::AeroCloud.Protocol.RouteDriverTime.DefaultInstance) return this;
        PrepareBuilder();
        if (other.HasRouteId) {
          RouteId = other.RouteId;
        }
        if (other.HasLineId) {
          LineId = other.LineId;
        }
        if (other.HasMaxTime) {
          MaxTime = other.MaxTime;
        }
        if (other.HasMinTime) {
          MinTime = other.MinTime;
        }
        if (other.HasBasedTime) {
          BasedTime = other.BasedTime;
        }
        if (other.HasIsEveryDay) {
          IsEveryDay = other.IsEveryDay;
        }
        if (other.HasStartDate) {
          StartDate = other.StartDate;
        }
        if (other.HasEndDate) {
          EndDate = other.EndDate;
        }
        this.MergeUnknownFields(other.UnknownFields);
        return this;
      }
      
      public override Builder MergeFrom(pb::ICodedInputStream input) {
        return MergeFrom(input, pb::ExtensionRegistry.Empty);
      }
      
      public override Builder MergeFrom(pb::ICodedInputStream input, pb::ExtensionRegistry extensionRegistry) {
        PrepareBuilder();
        pb::UnknownFieldSet.Builder unknownFields = null;
        uint tag;
        string field_name;
        while (input.ReadTag(out tag, out field_name)) {
          if(tag == 0 && field_name != null) {
            int field_ordinal = global::System.Array.BinarySearch(_routeDriverTimeFieldNames, field_name, global::System.StringComparer.Ordinal);
            if(field_ordinal >= 0)
              tag = _routeDriverTimeFieldTags[field_ordinal];
            else {
              if (unknownFields == null) {
                unknownFields = pb::UnknownFieldSet.CreateBuilder(this.UnknownFields);
              }
              ParseUnknownField(input, unknownFields, extensionRegistry, tag, field_name);
              continue;
            }
          }
          switch (tag) {
            case 0: {
              throw pb::InvalidProtocolBufferException.InvalidTag();
            }
            default: {
              if (pb::WireFormat.IsEndGroupTag(tag)) {
                if (unknownFields != null) {
                  this.UnknownFields = unknownFields.Build();
                }
                return this;
              }
              if (unknownFields == null) {
                unknownFields = pb::UnknownFieldSet.CreateBuilder(this.UnknownFields);
              }
              ParseUnknownField(input, unknownFields, extensionRegistry, tag, field_name);
              break;
            }
            case 8: {
              result.hasRouteId = input.ReadInt64(ref result.routeId_);
              break;
            }
            case 16: {
              result.hasLineId = input.ReadInt64(ref result.lineId_);
              break;
            }
            case 24: {
              result.hasMaxTime = input.ReadInt32(ref result.maxTime_);
              break;
            }
            case 32: {
              result.hasMinTime = input.ReadInt32(ref result.minTime_);
              break;
            }
            case 40: {
              result.hasBasedTime = input.ReadBool(ref result.basedTime_);
              break;
            }
            case 48: {
              result.hasIsEveryDay = input.ReadBool(ref result.isEveryDay_);
              break;
            }
            case 56: {
              result.hasStartDate = input.ReadInt64(ref result.startDate_);
              break;
            }
            case 64: {
              result.hasEndDate = input.ReadInt64(ref result.endDate_);
              break;
            }
          }
        }
        
        if (unknownFields != null) {
          this.UnknownFields = unknownFields.Build();
        }
        return this;
      }
      
      
      public bool HasRouteId {
        get { return result.hasRouteId; }
      }
      public long RouteId {
        get { return result.RouteId; }
        set { SetRouteId(value); }
      }
      public Builder SetRouteId(long value) {
        PrepareBuilder();
        result.hasRouteId = true;
        result.routeId_ = value;
        return this;
      }
      public Builder ClearRouteId() {
        PrepareBuilder();
        result.hasRouteId = false;
        result.routeId_ = 0L;
        return this;
      }
      
      public bool HasLineId {
        get { return result.hasLineId; }
      }
      public long LineId {
        get { return result.LineId; }
        set { SetLineId(value); }
      }
      public Builder SetLineId(long value) {
        PrepareBuilder();
        result.hasLineId = true;
        result.lineId_ = value;
        return this;
      }
      public Builder ClearLineId() {
        PrepareBuilder();
        result.hasLineId = false;
        result.lineId_ = 0L;
        return this;
      }
      
      public bool HasMaxTime {
        get { return result.hasMaxTime; }
      }
      public int MaxTime {
        get { return result.MaxTime; }
        set { SetMaxTime(value); }
      }
      public Builder SetMaxTime(int value) {
        PrepareBuilder();
        result.hasMaxTime = true;
        result.maxTime_ = value;
        return this;
      }
      public Builder ClearMaxTime() {
        PrepareBuilder();
        result.hasMaxTime = false;
        result.maxTime_ = 0;
        return this;
      }
      
      public bool HasMinTime {
        get { return result.hasMinTime; }
      }
      public int MinTime {
        get { return result.MinTime; }
        set { SetMinTime(value); }
      }
      public Builder SetMinTime(int value) {
        PrepareBuilder();
        result.hasMinTime = true;
        result.minTime_ = value;
        return this;
      }
      public Builder ClearMinTime() {
        PrepareBuilder();
        result.hasMinTime = false;
        result.minTime_ = 0;
        return this;
      }
      
      public bool HasBasedTime {
        get { return result.hasBasedTime; }
      }
      public bool BasedTime {
        get { return result.BasedTime; }
        set { SetBasedTime(value); }
      }
      public Builder SetBasedTime(bool value) {
        PrepareBuilder();
        result.hasBasedTime = true;
        result.basedTime_ = value;
        return this;
      }
      public Builder ClearBasedTime() {
        PrepareBuilder();
        result.hasBasedTime = false;
        result.basedTime_ = false;
        return this;
      }
      
      public bool HasIsEveryDay {
        get { return result.hasIsEveryDay; }
      }
      public bool IsEveryDay {
        get { return result.IsEveryDay; }
        set { SetIsEveryDay(value); }
      }
      public Builder SetIsEveryDay(bool value) {
        PrepareBuilder();
        result.hasIsEveryDay = true;
        result.isEveryDay_ = value;
        return this;
      }
      public Builder ClearIsEveryDay() {
        PrepareBuilder();
        result.hasIsEveryDay = false;
        result.isEveryDay_ = false;
        return this;
      }
      
      public bool HasStartDate {
        get { return result.hasStartDate; }
      }
      public long StartDate {
        get { return result.StartDate; }
        set { SetStartDate(value); }
      }
      public Builder SetStartDate(long value) {
        PrepareBuilder();
        result.hasStartDate = true;
        result.startDate_ = value;
        return this;
      }
      public Builder ClearStartDate() {
        PrepareBuilder();
        result.hasStartDate = false;
        result.startDate_ = 0L;
        return this;
      }
      
      public bool HasEndDate {
        get { return result.hasEndDate; }
      }
      public long EndDate {
        get { return result.EndDate; }
        set { SetEndDate(value); }
      }
      public Builder SetEndDate(long value) {
        PrepareBuilder();
        result.hasEndDate = true;
        result.endDate_ = value;
        return this;
      }
      public Builder ClearEndDate() {
        PrepareBuilder();
        result.hasEndDate = false;
        result.endDate_ = 0L;
        return this;
      }
    }
    static RouteDriverTime() {
      object.ReferenceEquals(global::AeroCloud.Protocol.Proto.RouteDriverTime.Descriptor, null);
    }
  }
  
  #endregion
  
}

#endregion Designer generated code
