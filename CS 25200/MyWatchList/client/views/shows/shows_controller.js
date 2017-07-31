this.ShowsController = RouteController.extend({
	template: "Shows",
	

	yieldTemplates: {
		/*YIELD_TEMPLATES*/
	},

	onBeforeAction: function() {
		this.next();
	},

	action: function() {
		if(this.isReady()) { this.render(); } else { this.render("loading"); }
		/*ACTION_FUNCTION*/
	},

	isReady: function() {
		

		var subs = [
			Meteor.subscribe("titles")
		];
		var ready = true;
		_.each(subs, function(sub) {
			if(!sub.ready())
				ready = false;
		});
		return ready;
	},

	data: function() {

		console.log(Meteor.userId().toString);

		var data = {
			params: this.params || {},
			titles: Titles.find({createdBy:Meteor.userId().toString()})
		};
		

		

		return data;
	},

	onAfterAction: function() {
		
	}
});