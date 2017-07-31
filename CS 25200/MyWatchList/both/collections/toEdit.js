

this.toEdit = {};

this.ToEdit = new Mongo.Collection("toEdit");

this.ToEdit.userCanInsert = function(userId, doc) {
    return true;
};

this.ToEdit.userCanUpdate = function(userId, doc) {
    return true;
};

this.ToEdit.userCanRemove = function(userId, doc) {
    return true;
};
