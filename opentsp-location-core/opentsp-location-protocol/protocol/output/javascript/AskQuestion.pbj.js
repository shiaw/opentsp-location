"use strict";
/** @suppress {duplicate}*/var platform;
if (typeof(platform)=="undefined") {platform = {};}
if (typeof(platform.auth)=="undefined") {platform.auth = {};}
platform.auth._PBJ_Internal="pbj-0.0.3";

platform.auth.AskQuestion = PROTO.Message("platform.auth.AskQuestion",{
	status: {
		options: {packed:true},
		multiplicity: PROTO.repeated,
		type: function(){return platform.auth.AskQuestion.QuestionStatus;},
		id: 1
	},
	content: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.string;},
		id: 2
	},
	answers: {
		options: {},
		multiplicity: PROTO.repeated,
		type: function(){return platform.auth.AskQuestion.CandidateAnswer;},
		id: 3
	},
	QuestionStatus: PROTO.Enum("platform.auth.AskQuestion.QuestionStatus",{
		emergency :0x00,
		terminalTTS :0x01,
		displayOnScreen :0x02	}),
CandidateAnswer : PROTO.Message("platform.auth.AskQuestion.CandidateAnswer",{
	answerId: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.int32;},
		id: 1
	},
	answerContent: {
		options: {},
		multiplicity: PROTO.required,
		type: function(){return PROTO.string;},
		id: 2
	}})
});
