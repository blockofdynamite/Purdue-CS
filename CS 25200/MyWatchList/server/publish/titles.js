Meteor.publish("titles", function(userId) {
	console.log(this.userId);
	return Titles.find({createdBy: this.userId});
});

Meteor.publish("titles_empty", function() {
	return Titles.find({_id:null}, {});
});

Meteor.publish("titles", function(titleId) {
	return Titles.find({_id:titleId}, {});
});

