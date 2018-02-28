//
// Created by dima on 26.02.18.
//

#ifndef HASHCODE_Slice_H
#define HASHCODE_Slice_H


class Slice {
public:
    int x, y, w, h;

    Slice() = default;

    Slice(int x, int y, int w, int h){
        this->x = x;
        this->y = y;
        this->w = w;
        this->h = h;
    }

    Slice(const Slice & sl){
        x = sl.x;
        y = sl.y;
        w = sl.w;
        h = sl.h;
    };

    Slice operator=( const Slice& sl){
        Slice rez(sl.x, sl.y, sl.w, sl.h);

        return rez;
    };

    bool operator<( const Slice& sl){
        if(x < sl.x){
            return true;
        }else if(x == sl.x){
            if(y < sl.y){
                return true;
            }else if(y == sl.y){
                if(w < sl.w){
                    return true;
                }else if(w == sl.w){
                    if(h < sl.h) {
                        return true;
                    }
                }
            }
        }

        return false;
    };

    friend bool operator<(const Slice& a, const Slice& sl){
        if(a.x < sl.x){
            return true;
        }else if(a.x == sl.x){
            if(a.y < sl.y){
                return true;
            }else if(a.y == sl.y){
                if(a.w < sl.w){
                    return true;
                }else if(a.w == sl.w){
                    if(a.h < sl.h) {
                        return true;
                    }
                }
            }
        }
    }

};


#endif //HASHCODE_Slice_H
