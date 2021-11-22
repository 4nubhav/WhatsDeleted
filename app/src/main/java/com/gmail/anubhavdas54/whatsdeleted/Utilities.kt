package com.gmail.anubhavdas54.whatsdeleted

class Utilities {
    companion object{
        /**
         * Check if a given msg is not a checking message
         * checking message is:<br/>
         * "Checking for new messages" on WhatsApp and
         * "Checking for new messages..." on Signal
         * @param msg String message to check
         * @return Boolean true if it is not a checking messages, false otherwise
         */
        @JvmStatic
        fun notCheckingMsg(msg:String?):Boolean{
            //is null msg not checking msg
            if (msg == null)return true

            return !msg.contains(Regex("Checking for [\\W\\w]* messages"))
        }
        /**
         * Check if a given msg is not a counting message
         * count message is(xxx and yy are numbers):<br/>
         * "xxx messages from yy chats" on WhatsApp and
         *
         * @param msg String message to check
         * @return Boolean true if it is not a checking messages, false otherwise
         */
        fun notCountMsg(msg: String?):Boolean{
            //is null msg not checking msg
            if (msg == null)return true

            return !msg.contains(Regex("[\\d]* messages from [\\d]* chats"))
        }
    }
}