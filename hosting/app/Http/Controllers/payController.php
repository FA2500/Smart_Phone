<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Billplz\Laravel\Billplz;
use Billplz\Client;
use Barryvdh\Debugbar\Facades\Debugbar;

use PDF;
use Dompdf\Dompdf;


class payController extends Controller
{
    public function index(Request $request)
    {
        $billplz = Client::make(config('services.billplz.key'),config('services.billplz.x-signature'));
        $billplz->useSandbox();
        
        $bill = $billplz->bill();

        /*$response = $bill->create(
            'hr10bp8q',
            $request->header("email"),
            $request->header("phone"),
            $request->header("name"),
            floatval($request->header("amount")),
            route("paycb"), 
            $request->header("desc"),
            ['redirect_url' => route("welcome") ] 
         )->toArray();*/

         $response = $bill->create(
            "hr10bp8q",
            "faris ammar",
            "01156403489",
            "mfarisamamr@gmail.com",
            550,
            route("paycb"), 
            "Hotel Booking",
            ['redirect_url' => route("welcome") ]
         )->toArray();

         //var_dump($bill);
         return redirect($response['url']); 
    }

    public function indextest(Request $request)
    {
        $billplz = Client::make(config('services.billplz.key'),config('services.billplz.x-signature'));
        $billplz->useSandbox();
        
        $bill = $billplz->bill();

        $response = $bill->create(
            "hr10bp8q",
            "faris ammar",
            550,
            route("paycb"), 
            "Hotel Booking",
            ['redirect_url' => route("welcome") ],
         )->toArray();

         //var_dump($bill);
         return redirect($response['url']); 
    }

    public function callback(Request $request)
    {
        //info($request);
        //$data = $request->except(['x_signature']);
        //$signature = new Signature(config('services.billplz.x-signature'), Signature::WEBHOOK_PARAMETERS);

        //if ($signature->verify($data, $request->x_signature)) {
            //$order = Order::where('invoice_number', $request->id)->first();
            if ($request->paid == 'true') {
                //$order->update(['status' => '4']);
                //LogBillplz::create($request->all());

                return response()->json(['success', true], 200);
            }
        //} 
        else {
            abort(403);
        }
    }

    public function invoice()
    {
        /*$pdf_doc = PDF::loadView('invoice');
        $pdf_doc->download('pdf.pdf');
        return view('invoice');*/
        $pdf_doc = PDF::loadView('invoice2');
        $pdf_doc->download('pdf.pdf');
        $this->invoices();
        return view('invoice2');
    }

    public function invoices()
    {
        $pdf_doc = PDF::loadView('invoice2');
        return $pdf_doc->download('invoice.pdf');
    }

    public function viewInvoice()
    {
        return view('invoice2');
    }
}
