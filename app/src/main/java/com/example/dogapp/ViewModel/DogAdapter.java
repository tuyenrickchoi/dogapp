    package com.example.dogapp.ViewModel;

    import android.content.Context;
    import android.os.Bundle;
    import android.view.GestureDetector;
    import android.view.LayoutInflater;
    import android.view.MotionEvent;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Filter;
    import android.widget.Filterable;

    import androidx.annotation.NonNull;
    import androidx.databinding.DataBindingUtil;
    import androidx.navigation.Navigation;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.dogapp.databinding.DogItemBinding;
    import com.example.dogapp.model.DogBreed;
    import com.example.dogapp.R;
    import com.squareup.picasso.Picasso;

    import java.util.ArrayList;
    import java.util.List;

    public class DogAdapter extends RecyclerView.Adapter<DogAdapter.DogViewHolder> implements Filterable{

        private static ArrayList<DogBreed> dogBreeds;
        private static ArrayList<DogBreed> dogBreedsCopy;

        public DogAdapter(ArrayList<DogBreed> dogBreeds) {
            this.dogBreeds = dogBreeds;
            this.dogBreedsCopy = dogBreeds;

        }

        @NonNull
        @Override
        public DogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    //        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dog_item, parent, false);
            DogItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.dog_item, parent, false);
            return new DogViewHolder(binding);

        }

        @Override
        public void onBindViewHolder(@NonNull DogViewHolder holder, int position) {
            DogBreed dogBreed = dogBreeds.get(position);
            holder.binding.setDog(dogBreed);
            Picasso.get().load(dogBreed.getUrl()).into(holder.binding.dogImage);
        }

        @Override
        public int getItemCount() {
            return dogBreeds.size();
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String input = charSequence.toString().toLowerCase();
                    ArrayList<DogBreed> filteredDog = new ArrayList<>();
                    if(input.isEmpty()) {
                        filteredDog.addAll(dogBreedsCopy);
                    }else {
                        for(DogBreed dog : dogBreedsCopy) {
                            if(dog.getName().toLowerCase().contains(input)) {
                                filteredDog.add(dog);
                            }
                        }
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = filteredDog;
                    return filterResults ;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    dogBreeds = new ArrayList<>();
                    dogBreeds.addAll((List)results.values);
                    notifyDataSetChanged();
                }
            };
        }

        public class DogViewHolder extends RecyclerView.ViewHolder{
            public DogItemBinding binding;

            public DogViewHolder(DogItemBinding itemBinding) {
                super(itemBinding.getRoot());
                this.binding = itemBinding;

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DogBreed dog = dogBreeds.get(getAdapterPosition());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("dogBreed", dog);
                        Navigation.findNavController(itemView).navigate(R.id.detailFragment, bundle);
                    }
                });
                itemView.setOnTouchListener(new OnTouchSwipeListener(itemView.getContext(), itemView) {
                    @Override
                    public void onSwipeLeft() {
                        if(binding.layout1.getVisibility() == View.GONE) {
                            binding.layout1.setVisibility(View.VISIBLE);
                            binding.layout3.setVisibility(View.GONE);
                        }else{
                            binding.layout1.setVisibility(View.GONE);
                            binding.layout3.setVisibility(View.VISIBLE);
                        }
                        super.onSwipeLeft();
                    }
                });
            }
        }
        public class OnTouchSwipeListener implements View.OnTouchListener {

            private final GestureDetector gestureDetector = new GestureDetector(new GestureListener());
            private final View view;

            public OnTouchSwipeListener(Context context, View view) {
                this.view = view;

            }

            public void onClick() {
            }

            public void onSwipeLeft() {
            }

            public void onSwipeRight() {
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }

            private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

                private static final int SWIPE_DISTANCE_THRESHOLD = 100;
                private static final int SWIPE_VELOCITY_THRESHOLD = 100;

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    view.performClick(); // Manually trigger the click event on the view
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    float distanceX = e2.getX() - e1.getX();
                    float distanceY = e2.getY() - e1.getY();
                    if (Math.abs(distanceX) > Math.abs(distanceY)
                            && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD
                            && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (distanceX > 0)
                            onSwipeRight();
                        else
                            onSwipeLeft();
                        return true;
                    }
                    return false;
                }
            }
        }
    }