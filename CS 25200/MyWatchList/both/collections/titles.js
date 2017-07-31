this.Titles = new Mongo.Collection("titles");

this.Titles.userCanInsert = function(userId, doc) {
	return true;
};

this.Titles.userCanUpdate = function(userId, doc) {
	return true;
};

this.Titles.userCanRemove = function(userId, doc) {
	return true;
};


this.updateId = '';
this.updateObject = null;
this.removeObject = null;