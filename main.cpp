#include <bits/stdc++.h>
#include "Slice.h"

#define _DEBUG 1

int w, h, l, r; // size of pizza, number ingridients in slice and maximu size of a slice

std::vector <int> pizza;
std::vector <int> up_sum_tomatoes; // number of tomatoes before in column

std::set <Slice> bad_slices;


bool divideFull(int x, int y, int cw, int ch, std::vector<Slice>& v){
    if(bad_slices.find({x,y,cw,ch}) != bad_slices.end())
        return false;

    if(cw * ch <= r){
        int numt = 0, // number tomatoes in current slice
            numm = 0; // number mushrooms in current slice

        for(int i = x; i < x + cw; i++){
            if(y > 0) {
                numt += up_sum_tomatoes[(y + ch - 1) * w + i] - up_sum_tomatoes[(y - 1) * w + i];
            }else{
                numt += up_sum_tomatoes[(y + ch - 1) * w + i];
            }
        }

        numm = cw * ch - numt;

        if(numm >= l && numt >= l){
            v.emplace_back(x, y, cw, ch);
            return true;
        }else{
            return false;
        }


    }else{
        // divide vertically
        for(int i = 1; i < cw; i++){
            bool t = true;
            std::vector <Slice> l;
            t &= divideFull(x, y, i, ch, l);


            if(t)
                t &= divideFull(x + i, y, cw - i, ch, l);

            if(t){
                v.insert(v.end(), l.begin(), l.end());
                return true;
            }

        }

        // divide horizontally
        for(int i = 1; i < ch; i++){
            bool t = true;
            std::vector <Slice> u;

            t &= divideFull(x, y, cw, i, u);
            if(t)
                t &= divideFull(x, y+i, cw, ch-i, u);

            if(t){
                v.insert(v.end(), u.begin(), u.end());
                return true;
            }

        }

        return false;
    }
}


std::shared_ptr<std::vector<Slice>> divideBest(int x, int y, int cw, int ch, int &outSize){
    if(cw * ch <= r){
        int numt = 0, // number tomatoes in current slice
            numm = 0; // number mushrooms in current slice

        for(int i = x; i < x + cw; i++){
            if(y > 0) {
                numt += up_sum_tomatoes[(y + ch - 1) * w + i] - up_sum_tomatoes[(y - 1) * w + i];
            }else{
                numt += up_sum_tomatoes[(y + ch - 1) * w + i];
            }
        }

        numm = cw * ch - numt;

        if(numm >= l && numt >= l){
            outSize = cw*ch;
            std::shared_ptr<std::vector<Slice>> v = std::make_shared<std::vector<Slice>>();
            v->emplace_back(x, y, cw, ch);
            return v;
        }else{
            outSize = 0;
            return std::shared_ptr<std::vector<Slice>>();
        }


    }else{
        // divide vertically
        int bestSize = 0;
        std::shared_ptr<std::vector<Slice> > v = std::make_unique<std::vector<Slice> >(), b1,b2;

        for(int i = 1; i < cw; i++){
            std::shared_ptr<std::vector<Slice> > l,r;
            int currentSizeL = 0, currentSizeR = 0;
            l = divideBest(x, y, i, ch, currentSizeL);
            r = divideBest(x + i, y, cw - i, ch, currentSizeR);
            int cSize = currentSizeL+currentSizeR;
            if(cSize > bestSize){
                b1 = l;
                b2 = r;
                bestSize = cSize;
            }

        }

        // divide horizontally
        for(int i = 1; i < ch; i++){
            std::shared_ptr<std::vector<Slice> > l,r;
            int currentSizeL = 0, currentSizeR = 0;
            l = divideBest(x, y, cw, i, currentSizeL);
            r = divideBest(x, y+i, cw, ch-i, currentSizeR);
            int cSize = currentSizeL+currentSizeR;
            if(cSize > bestSize){
                b1 = l;
                b2 = r;
                bestSize = cSize;
            }
        }
        outSize = bestSize;
        if(outSize != 0) {
            if(b1)
                v->insert(v->end(), b1->begin(), b1->end());
            if(b2)
                v->insert(v->end(), b2->begin(), b2->end());
        }
        return v;
    }
}

void exportSlices(const std::string output, std::vector<Slice>& sl){
    std::ofstream out(output);

    out<<sl.size()<<std::endl;

    for(auto &e: sl){
        out<<e.y<<" "<<e.x<<" "<<e.y + e.h - 1<<" "<<e.x + e.w - 1<<std::endl;
    }

    out.close();
}

void solve(const std::string & input, const std::string & output){
    std::ifstream in(input);

    in>>h>>w>>l>>r;
    std::cout<<"width = "<<w<<std::endl
             <<"height = "<<h<<std::endl
             <<"l = "<<l<<std::endl
             <<"r = "<<r<<std::endl;

    pizza.assign(w*h, 0);
    up_sum_tomatoes.assign(w*h, 0);

    for(int i = 0; i < h; i++){
        for(int j = 0; j < w; j++){
            char c; in>>c;
            pizza[i*w + j] = c == 'T' ? 1 : 0;
            up_sum_tomatoes[i*w + j] = (i != 0)
                                       ? (pizza[i*w + j] + up_sum_tomatoes[(i-1)*w + j])
                                       : ((c == 'T') ?1:0);


        }
    }


    if(_DEBUG >= 2) {
        std::cout << std::endl;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
            }
            std::cout << std::endl;
        }
        std::cout << std::endl;


        for(int i = 0; i< h; i++){
            for(int j= 0; j< w; j++){
                std::cout<<up_sum_tomatoes[i*w+j];
            }
            std::cout<<std::endl;
        }
    }



    const int kBlock = 8;

    std::vector <Slice> sl; // fixed slices

    time_t start = clock();

    // For small pizza run full search
    if(w*h <= kBlock*kBlock){
        std::cout<<"Small pizza"<<std::endl;
        divideFull(0, 0, w, h, sl);
        exportSlices(output, sl);
        return;
    }

    // For small l and r, we can divide pizza in blocks, and run full search on each block
    // I think it will be good enough


    int i = 0;
    int j = 0;

    if(std::max(l,r) < kBlock*kBlock/4){
        std::cout<<"Big pizza with small slices"<<std::endl;

        int count = 0;
        int total = (h/kBlock)*(w/kBlock);
        for(i = 0; i < h; i+=kBlock){
            for(j = 0; j < w; j+=kBlock){
                int size = 0;
                count++;
                std::shared_ptr <std::vector<Slice> > cv;
                std::cout<<"runnin block "<<count<<"/"<<total<<std::endl;
                cv  = divideBest(j, i, kBlock, kBlock, size);
                std::cout<<"size "<<size<<std::endl;
                if(size)
                    sl.insert(sl.end(), cv->begin(), cv->end());
                exportSlices(output, sl);
            }
            int size = 0;
            std::cout<<"runnin subblock "<<count<<"/"<<total<<std::endl;
            std::shared_ptr <std::vector<Slice> >  cv  = divideBest(j-kBlock, i, j-w, kBlock, size);
            std::cout<<"size "<<size<<std::endl;
            if(size)
                sl.insert(sl.end(), cv->begin(), cv->end());

        }

        return;
    }


    time_t end = clock();



    std::cout.setf(std::ios::fixed);
    std::cout<<"time : " << (float)(end - start) / CLOCKS_PER_SEC<<std::endl;



}




int main(int argc, char* argv[]) {
   if(argc < 2){
       std::cout<<"no parameters"<<std::endl;
       return 1;
   }else{
       solve("./in/" + std::string(argv[1])+".in", "./out/" + std::string(argv[1])+".out");
   }

   return 0;
}