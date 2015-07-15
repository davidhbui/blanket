

Parse.Cloud.afterSave("Donation", function(request) {
 
var check = request.object.get("isPush");
if (check == 1) {
 
    Parse.Push.send({
    //where: {},
    channels: [ "ch" ],
    data: {
      alert: "Your donation has been redeemed!"
    }
  }, {
    success: function() {
      // Push was successful
    },
    error: function(error) {
      // Handle error
      throw "Got an error " + error.code + " : " + error.message;
    }
  });
 
}
});


Parse.Cloud.define("chargeCard", function(request, response) {
  var id = request.params.tokenID;
  var amount = request.params.amount;
  var Stripe = require('stripe');
  Stripe.initialize('sk_test_LLO6RDfNPhM1TdHNucNN01H8');
  Stripe.Charges.create({
  amount: amount * 100, // $10 expressed in cents
  currency: 'usd',
  card: id // the token id should be sent from the client
},{
  success: function(httpResponse) {
    response.success("Purchase made!");
  },
  error: function(httpResponse) {
    response.error("Uh oh, something went wrong");
  }
});
});

/*
Parse.Cloud.define("newCard", function(request, response) {


Parse.Push.send({
    //where: {},
    channels: [ "ch" ],
    data: {
      alert: "Charging"
    }
  }, {
    success: function() {
      // Push was successful
    },
    error: function(error) {
      // Handle error
      throw "Got an error " + error.code + " : " + error.message;
    }
  });

	var stripe = require("stripe")("sk_test_LLO6RDfNPhM1TdHNucNN01H8");

	// (Assuming you're using express - expressjs.com)
	// Get the credit card details submitted by the form
	var stripeToken = request.object.get("token");

	var charge = stripe.charges.create({
	  amount: 1000, // amount in cents, again
	  currency: "usd",
	  source: stripeToken,
	  description: "Example charge"
	}, function(err, charge) {
	  if (err && err.type === 'StripeCardError') {
	    // The card has been declined
	  }
	});
});
*/