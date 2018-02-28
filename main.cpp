#include <bits/stdc++.h>
#include "Slice.h"

#define _DEBUG 1

int w, h, l, r; // size of pizza, number ingridients in slice and maximu size of a slice

std::vector <int> pizza;
std::vector <int> up_sum_tomatoes; // number of tomatoes before in column

std::set <Slice> bad_slices;


bool divide(int x, int y, int cw, int ch, std::vector<Slice>& v){
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
            bad_slices.insert({x,y,cw,ch});
            return false;
        }


    }else{
        // divide vertically
        for(int i = 1; i < cw; i++){
            bool t = true;
            std::vector <Slice> l;
            t &= divide(x, y, i, ch, l);


            if(t)
                t &= divide(x + i, y, cw - i, ch, l);

            if(t){
                v.insert(v.end(), l.begin(), l.end());
                return true;
            }

        }

        // divide horizontally
        for(int i = 1; i < ch; i++){
            bool t = true;
            std::vector <Slice> u;

            t &= divide(x, y, cw, i, u);
            if(t)
                t &= divide(x, y+i, cw, ch-i, u);

            if(t){
                v.insert(v.end(), u.begin(), u.end());
                return true;
            }

        }

        bad_slices.insert({x,y,cw,ch});
        return false;
    }
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

    const int kBlock = 16;

    std::cout<<std::endl;
    std::vector <Slice> sl;
    int i = 0;
    int j = 0;

    time_t start = clock();

    divide(i, j, w, h, sl);


    time_t end = clock();

    for(auto &e: sl){
        std::cout<<e.y<<" "<<e.x<<" "<<e.y+e.h-1<<" "<<e.x+e.w-1<<std::endl;
    }


    std::cout.setf(std::ios::fixed);
    std::cout<<"time : " << (float)(end - start) / CLOCKS_PER_SEC<<std::endl;

    std::ofstream out(output);

    out<<sl.size()<<std::endl;

    for(auto &e: sl){
        out<<e.y<<" "<<e.x<<" "<<e.y + e.h - 1<<" "<<e.x + e.w - 1<<std::endl;
    }

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