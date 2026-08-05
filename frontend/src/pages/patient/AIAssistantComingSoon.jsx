import { Bot, Sparkles, Brain, ArrowRight } from 'lucide-react';

export default function AIAssistantComingSoon() {
  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-semibold text-gray-900">AI Health Assistant</h2>
      </div>

      <div className="bg-white rounded-2xl shadow-xl border border-gray-100 overflow-hidden relative">
        {/* Background decorative elements */}
        <div className="absolute top-0 right-0 -mr-20 -mt-20 w-64 h-64 rounded-full bg-gradient-to-br from-indigo-100 to-purple-100 opacity-50 blur-3xl"></div>
        <div className="absolute bottom-0 left-0 -ml-20 -mb-20 w-64 h-64 rounded-full bg-gradient-to-tr from-blue-100 to-cyan-100 opacity-50 blur-3xl"></div>
        
        <div className="relative p-8 md:p-12 text-center">
          <div className="w-24 h-24 mx-auto bg-gradient-to-br from-indigo-500 to-purple-600 rounded-2xl shadow-lg flex items-center justify-center transform -rotate-6 mb-8 mt-4 relative">
            <Bot className="w-12 h-12 text-white transform rotate-6" />
            <Sparkles className="w-6 h-6 text-yellow-300 absolute -top-3 -right-3 animate-pulse" />
          </div>
          
          <h3 className="text-3xl font-extrabold text-transparent bg-clip-text bg-gradient-to-r from-indigo-600 to-purple-600 mb-4">
            Coming Soon: Your Personal AI Health Guide
          </h3>
          
          <p className="text-xl text-gray-600 max-w-2xl mx-auto mb-10">
            We're building a next-generation AI assistant to help you understand your medical records, prepare for appointments, and navigate your health journey with confidence.
          </p>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-6 max-w-4xl mx-auto mb-12">
            <div className="bg-white/50 backdrop-blur-sm p-6 rounded-xl border border-indigo-50 shadow-sm text-left">
              <div className="w-10 h-10 bg-indigo-100 text-indigo-600 rounded-lg flex items-center justify-center mb-4">
                <Brain className="w-5 h-5" />
              </div>
              <h4 className="font-semibold text-gray-900 mb-2">Smart Analysis</h4>
              <p className="text-sm text-gray-600">Instantly translate complex medical jargon from your clinical notes and radiology reports into plain English.</p>
            </div>
            
            <div className="bg-white/50 backdrop-blur-sm p-6 rounded-xl border border-purple-50 shadow-sm text-left">
              <div className="w-10 h-10 bg-purple-100 text-purple-600 rounded-lg flex items-center justify-center mb-4">
                <Bot className="w-5 h-5" />
              </div>
              <h4 className="font-semibold text-gray-900 mb-2">24/7 Availability</h4>
              <p className="text-sm text-gray-600">Ask questions about your prescriptions, upcoming procedures, or general health concerns at any time.</p>
            </div>
            
            <div className="bg-white/50 backdrop-blur-sm p-6 rounded-xl border border-blue-50 shadow-sm text-left">
              <div className="w-10 h-10 bg-blue-100 text-blue-600 rounded-lg flex items-center justify-center mb-4">
                <Sparkles className="w-5 h-5" />
              </div>
              <h4 className="font-semibold text-gray-900 mb-2">Personalized Insights</h4>
              <p className="text-sm text-gray-600">Receive proactive health insights based on your unique medical history and timeline.</p>
            </div>
          </div>

          <button 
            type="button" 
            className="inline-flex items-center px-6 py-3 border border-transparent text-base font-medium rounded-full shadow-sm text-white bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 transition-all transform hover:scale-105"
            onClick={() => alert("You've been added to the waitlist! We'll notify you when the AI Assistant is ready.")}
          >
            Join the Waitlist
            <ArrowRight className="ml-2 -mr-1 w-5 h-5" />
          </button>
        </div>
      </div>
    </div>
  );
}
