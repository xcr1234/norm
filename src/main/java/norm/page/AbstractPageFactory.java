package norm.page;


import java.io.Serializable;

public abstract class AbstractPageFactory implements PageFactory{




    protected static abstract class AbstractPage implements Page,Serializable{
        private static final long serialVersionUID = -405234936122893952L;
        private int pageNumber;
        private int pageSize;

        public AbstractPage(int pageNumber, int pageSize) {
            if(pageNumber < 1){
                throw new IllegalArgumentException("pageNumber must be larger than 1");
            }
            if(pageSize < 1){
                throw new IllegalArgumentException("pageNumber must be larger than 0");
            }
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
        }


        public int limit() {
            return pageSize;
        }


        public int offset() {
            return (pageNumber - 1) * pageSize;
        }


        public int from() {
            return (pageNumber - 1) * pageSize;
        }


        public int to() {
            return pageNumber * pageSize;
        }

        @Override
        public String toString() {
            return "Page{limit:"+limit()+",offset:"+offset()+",from:"+from()+",to:"+to()+"}";
        }
    }

}
