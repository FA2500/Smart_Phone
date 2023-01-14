const Billplz = require('billplz');
const billplz = new Billplz({
    'key': 'bd3b9b73-7ae2-4c9c-8a37-b2382854556d',
    'sandbox': true
  });

var info = {};

window.onload = function() {
  getBill();
};

function getBill()
{
  billplz.create_bill({
    'collection_id': 'hr10bp8q',
    'description': 'Mee Segera Sedap 200g',
    'email': 'sukamakan@meesegera.com',
    'name': 'Ahmad Segera',
    'amount': 550, //RM5.50
    'callback_url': "http://example.com/webhook/",
    'redirect_url': "http://example.com/thank-you",
    'due_at': '2016-08-31'
  }, function(err, res) {
    //info['url'] = res.url;
    console.log(res.url);
  });

  return res.url;
}

