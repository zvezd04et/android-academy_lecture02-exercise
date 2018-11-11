package com.z.newsleak.utils;

import com.z.newsleak.data.Category;
import com.z.newsleak.model.NewsItem;
import com.z.newsleak.network.dto.ImageDTO;
import com.z.newsleak.network.dto.NewsItemDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NewsItemConverter {

    private final static String IMAGE_FORMAT = "Normal";
    private final static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    @NonNull
    public static List<NewsItem> convertFromDtos(@NonNull List<NewsItemDTO> newsItemDTOs, @Nullable Category currentCategory) {

        final List<NewsItem> news = new ArrayList<>();

        for (NewsItemDTO newsItemDTO : newsItemDTOs) {
            final String previewImageUrl = getImageUrl(newsItemDTO.getMultimedia());
            final Category category = getCategory(newsItemDTO.getSection(), currentCategory);
            final Date publishDate = getPublishDate(newsItemDTO.getPublishedDate());

            final NewsItem newsItem = new NewsItem.Builder(category)
                    .section(newsItemDTO.getSection())
                    .title(newsItemDTO.getTitle())
                    .imageUrl(previewImageUrl)
                    .previewText(newsItemDTO.getAbstractField())
                    .publishDate(publishDate)
                    .articleUrl(newsItemDTO.getUrl())
                    .build();
            news.add(newsItem);
        }

        return news;
    }

    @Nullable
    private static String getImageUrl(@Nullable List<ImageDTO> multimedia) {

        if (multimedia == null) {
            return null;
        }

        if (multimedia.size() == 0) {
            return null;
        }

        String previewImageUrl = null;
        for (ImageDTO imageDTO : multimedia) {
            if (IMAGE_FORMAT.equals(imageDTO.getFormat())) {
                previewImageUrl = imageDTO.getUrl();
                break;
            }
        }
        return previewImageUrl;
    }

    @NonNull
    private static Category getCategory(@Nullable String section, @Nullable Category currentCategory) {

        if (section == null) {
            return Category.HOME;
        }

        for (Category category : Category.values()) {
            if (section.equals(category.getSection())) {
                return category;
            }
        }

        return (currentCategory != null) ? currentCategory : Category.HOME;
    }

    @Nullable
    private static Date getPublishDate(@Nullable String publishDate) {

        final SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.US);
        Date formattedDate = null;
        try {
            formattedDate = formatter.parse(publishDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;

    }

    private NewsItemConverter() {
        throw new IllegalAccessError("Attempt to instantiate utility class.");
    }
}
